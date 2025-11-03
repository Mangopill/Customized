package mangopill.customized.common.block.entity;

import mangopill.customized.common.recipe.*;
import mangopill.customized.common.util.CreateItemStackHandler;
import mangopill.customized.common.util.CItemStackHandlerHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.*;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

import static mangopill.customized.common.util.CItemStackHandlerHelper.*;

public abstract class CBasicCookingBlockEntity<T extends CRecipeInterface<RecipeWrapper>> extends BlockEntity implements CreateItemStackHandler {
    protected final int inputSlot;
    protected final int outputSlot;
    protected final int allSlot;
    protected final RecipeManager.CachedCheck<RecipeWrapper, T> check;
    protected final ItemStackHandler itemStackHandler;
    protected int cookingTime;
    protected int cookingCompletionTime;

    protected CBasicCookingBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState,
                                       int inputSlot, int outputSlot, RecipeManager.CachedCheck<RecipeWrapper, T> check) {
        super(type, pos, blockState);
        this.inputSlot = inputSlot;
        this.outputSlot = outputSlot;
        this.allSlot = inputSlot + outputSlot;
        this.check = check;
        this.itemStackHandler = createItemStackHandler(allSlot);
    }

    @Override
    public void itemStackHandlerChanged() {
        super.setChanged();
        if (level != null){
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
        }
    }

    public static <C extends CBasicCookingBlockEntity<?>> void cookingTick(Level level, BlockPos pos, BlockState state, C blockEntity) {
        RecipeWrapper wrapper = new RecipeWrapper(blockEntity.itemStackHandler);
        blockEntity.cookingTick(level, pos, state, wrapper);
        blockEntity.itemStackHandlerChanged();
    }

    public void cookingTick(Level level, BlockPos pos, BlockState state, RecipeWrapper wrapper) {
        Optional<RecipeHolder<T>> matchRecipe = getMatchRecipe(wrapper);
        if (matchRecipe.isEmpty() || !canCookRecipe(matchRecipe.get().value(),wrapper)){
            return;
        }
        if (!hasInput()) {
            return;
        }
        cookRecipe(level, matchRecipe.get(), pos, state);
    }

    abstract protected void cookRecipe(Level level, RecipeHolder<T> holder, BlockPos pos, BlockState state);

    protected boolean canCookRecipe(T recipe, RecipeWrapper recipeWrapper) {
        ItemStack resultStack = recipe.getResultItem(this.level.registryAccess());
        return !resultStack.isEmpty() && recipe.matches(recipeWrapper, this.level);
    }

    protected Optional<RecipeHolder<T>> getMatchRecipe(RecipeWrapper recipeWrapper) {
        return hasInput() && level != null
                ? check.getRecipeFor(recipeWrapper, this.level)
                : Optional.empty();
    }

    public void interact(ItemStack itemStackInHand, Player player, Level level, InteractionHand hand, BlockState state,
                         BlockPos pos, SoundEvent output, SoundEvent insert) {
        if (itemStackInHand.isEmpty() && player.isShiftKeyDown()) {
            outputItem(level, state, pos);
            level.playSound(null, pos, output, SoundSource.BLOCKS, 0.8F, 1.0F);
        } else {
            insertItem(itemStackInHand);
            level.playSound(null, pos, insert, SoundSource.BLOCKS, 0.8F, 1.0F);
        }
        itemStackHandlerChanged();
    }

    public void outputItem(Level level, BlockState state, BlockPos pos) {
        getItemStackListInBlockEntity(true).forEach(itemStack -> spawnItemEntity(level, itemStack, state, pos));
    }

    public void insertItem(ItemStack itemStackInHand) {
        CItemStackHandlerHelper.fillInItem(itemStackHandler, itemStackInHand, 0, allSlot);
        itemStackHandlerChanged();
    }

    public boolean hasInput() {
        return CItemStackHandlerHelper.hasInput(itemStackHandler, inputSlot);
    }

    public List<ItemStack> getItemStackListInBlockEntity(boolean includeOutput) {
        if (includeOutput){
            return CItemStackHandlerHelper.getItemStackListInSlot(itemStackHandler, 0, allSlot);
        } else {
            return CItemStackHandlerHelper.getItemStackListInSlot(itemStackHandler, 0, inputSlot);
        }
    }

    @Override
    @Nullable
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void loadAdditional(CompoundTag compound, HolderLookup.Provider registries) {
        super.loadAdditional(compound, registries);
        itemStackHandler.deserializeNBT(registries, compound.getCompound("ItemStackHandler"));
        cookingTime = compound.getInt("CookingTime");
        cookingCompletionTime = compound.getInt("CookingCompletionTime");
    }

    @Override
    public void saveAdditional(CompoundTag compound, HolderLookup.Provider registries) {
        super.saveAdditional(compound, registries);
        compound.putInt("CookingTime", cookingTime);
        compound.putInt("CookingCompletionTime", cookingCompletionTime);
        compound.put("ItemStackHandler", itemStackHandler.serializeNBT(registries));
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag, registries);
        return tag;
    }

    public int getInputSlot() {
        return inputSlot;
    }

    public int getOutputSlot() {
        return outputSlot;
    }

    public int getAllSlot() {
        return allSlot;
    }

    public RecipeManager.CachedCheck<RecipeWrapper, T> getCheck() {
        return check;
    }

    public ItemStackHandler getItemStackHandler() {
        return itemStackHandler;
    }

    public int getCookingTime() {
        return cookingTime;
    }

    public int getCookingCompletionTime() {
        return cookingCompletionTime;
    }
}
