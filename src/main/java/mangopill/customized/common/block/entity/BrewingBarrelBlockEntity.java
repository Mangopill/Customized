package mangopill.customized.common.block.entity;

import mangopill.customized.common.block.BrewingBarrelBlock;
import mangopill.customized.common.block.handler.BrewingBarrelItemHandler;
import mangopill.customized.common.recipe.BrewingBarrelRecipe;
import mangopill.customized.common.registry.*;
import mangopill.customized.common.util.CItemStackHandlerHelper;
import mangopill.customized.common.util.record.UUIDRecord;
import net.minecraft.core.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.*;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;

import java.util.*;

import static mangopill.customized.common.util.CompoundTagHelper.*;
import static mangopill.customized.common.util.CItemStackHandlerHelper.*;

public class BrewingBarrelBlockEntity extends CBasicCookingBlockEntity<BrewingBarrelRecipe> {
    private final IItemHandler inputAndOutputHandler;
    private Ingredient containerItem;
    private UUID lastInteractPlayerId;

    public BrewingBarrelBlockEntity(BlockPos pos, BlockState blockState) {
        super(CBlockEntityTypeRegistry.BREWING_BARREL.get(), pos, blockState, 4, 1, RecipeManager.createCheck(CRecipeRegistry.BREWING_BARREL.get()));
        this.inputAndOutputHandler = new BrewingBarrelItemHandler(this, itemStackHandler);
        this.containerItem = Ingredient.EMPTY;
        this.lastInteractPlayerId = UUIDRecord.NULL.uuid();
    }

    @Override
    protected void cookRecipe(Level level, RecipeHolder<BrewingBarrelRecipe> holder, BlockPos pos, BlockState state) {
        ++cookingTime;
        getRecipeCookingCompletionTime(holder);
        if (cookingTime < cookingCompletionTime) {
            return;
        }
        Block block = level.getBlockState(pos).getBlock();
        if (block instanceof BrewingBarrelBlock) {
            if (state.getValue(BrewingBarrelBlock.PROGRESS) < 12){
                this.clearCookingTimeAndUpdate(pos, state, state.getValue(BrewingBarrelBlock.PROGRESS) + 1);
                return;
            }
            if (!containsSameItem(List.of(containerItem.getItems()), itemStackHandler.getStackInSlot(inputSlot))) {
                return;
            }
            ItemStack resultStack = holder.value().getResultItem(this.level.registryAccess()).copy();
            spawnItemEntity(level, resultStack.copy(), state, pos);
            for (int i = 0; i < inputSlot + outputSlot; ++i) {
                ItemStack slotStack = itemStackHandler.getStackInSlot(i);
                spawnUsingConvertsTo(level, List.of(slotStack), state, pos);
                if (!slotStack.isEmpty()){
                    slotStack.shrink(1);
                }
            }
            if (level.getPlayerByUUID(lastInteractPlayerId) instanceof ServerPlayer serverPlayer) {
                CAdvancementRegistry.USE_BREWING_BARREL.get().trigger(serverPlayer);
            }
            clearCookingTimeAndUpdate(pos, state, 0);
            containerItem = Ingredient.EMPTY;
        }
    }

    @Override
    protected boolean canCookRecipe(BrewingBarrelRecipe recipe, RecipeWrapper recipeWrapper) {
        containerItem = recipe.getContainerItem();
        return super.canCookRecipe(recipe, recipeWrapper);
    }

    @Override
    public void interact(ItemStack itemStackInHand, Player player, Level level, InteractionHand hand, BlockState state,
                         BlockPos pos, SoundEvent output, SoundEvent insert){
        super.interact(itemStackInHand, player, level, hand, state, pos, output, insert);
        lastInteractPlayerId = player.getUUID();
    }

    @Override
    public void insertItem(ItemStack itemStackInHand) {
        if (containsSameItem(List.of(containerItem.getItems()), itemStackInHand)) {
            CItemStackHandlerHelper.fillInItem(itemStackHandler, itemStackInHand, inputSlot, inputSlot + outputSlot);
        } else {
            CItemStackHandlerHelper.fillInItem(itemStackHandler, itemStackInHand, 0, inputSlot);
        }
        itemStackHandlerChanged();
    }

    protected void clearCookingTimeAndUpdate(BlockPos pos, BlockState state, int value) {
        if (cookingTime != 0){
            cookingTime = 0;
        }
        if (level != null) {
            level.setBlockAndUpdate(pos, state.setValue(BrewingBarrelBlock.PROGRESS, value));
        }
        itemStackHandlerChanged();
    }

    public void getRecipeCookingCompletionTime(RecipeHolder<BrewingBarrelRecipe> holder){
        cookingCompletionTime = holder.value().getCookingTime();
    }

    @Override
    public void loadAdditional(CompoundTag compound, HolderLookup.Provider registries) {
        super.loadAdditional(compound, registries);
        if (compound.contains("Container")) {
            containerItem = deserializeIngredientNBT(compound);
        }
        lastInteractPlayerId = compound.getUUID("LastInteractPlayerId");
    }

    @Override
    public void saveAdditional(CompoundTag compound, HolderLookup.Provider registries) {
        super.saveAdditional(compound, registries);
        putIngredientTag(compound, containerItem);
        compound.putUUID("LastInteractPlayerId", lastInteractPlayerId);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag, registries);
        return tag;
    }

    public IItemHandler getInputAndOutputHandler() {
        return inputAndOutputHandler;
    }

    public Ingredient getContainerItem() {
        return containerItem;
    }

    public UUID getLastInteractPlayerId() {
        return lastInteractPlayerId;
    }
}
