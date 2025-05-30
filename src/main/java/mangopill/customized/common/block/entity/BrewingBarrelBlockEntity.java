package mangopill.customized.common.block.entity;

import mangopill.customized.common.block.BrewingBarrelBlock;
import mangopill.customized.common.block.handler.BrewingBarrelItemHandler;
import mangopill.customized.common.recipe.BrewingBarrelRecipe;
import mangopill.customized.common.registry.*;
import mangopill.customized.common.util.CreateItemStackHandler;
import mangopill.customized.common.util.ModItemStackHandlerHelper;
import net.minecraft.core.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class BrewingBarrelBlockEntity extends BlockEntity implements CreateItemStackHandler {
    private final int inputSlot;
    private final int outputSlot;
    private final int allSlot;
    private final RecipeManager.CachedCheck<RecipeWrapper, BrewingBarrelRecipe> check;
    private final ItemStackHandler itemStackHandler;
    private final LazyOptional<IItemHandler> inputHandler;
    private final LazyOptional<IItemHandler> outputHandler;
    private ItemStack containerItem;
    private int cookingTime;
    private int cookingCompletionTime;

    public BrewingBarrelBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntityTypeRegistry.BREWING_BARREL.get(), pos, blockState);
        this.inputSlot = 4;
        this.outputSlot = 1;
        this.allSlot = inputSlot + outputSlot;
        this.itemStackHandler = createItemStackHandler(allSlot);
        this.inputHandler =  LazyOptional.of(() -> new BrewingBarrelItemHandler(itemStackHandler, Direction.UP, inputSlot));
        this.outputHandler = LazyOptional.of(() -> new BrewingBarrelItemHandler(itemStackHandler, Direction.DOWN, inputSlot));
        this.containerItem = ItemStack.EMPTY;
        this.check = RecipeManager.createCheck(ModRecipeRegistry.BREWING_BARREL.get());
    }

    @Override
    public <T> @Nonnull LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            if (side == Direction.UP) {
                return inputHandler.cast();
            }
            return outputHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    public static void cookingTick(Level level, BlockPos pos, BlockState state, BrewingBarrelBlockEntity barrelBlockEntity) {
        RecipeWrapper wrapper = new RecipeWrapper(barrelBlockEntity.itemStackHandler);
        Optional<BrewingBarrelRecipe> barrelMatchRecipe = barrelBlockEntity.getBrewingBarrelMatchRecipe(wrapper);
        if (barrelMatchRecipe.isPresent() && barrelBlockEntity.canCookRecipe(barrelMatchRecipe.get(),wrapper)){
            barrelBlockEntity.cookRecipe(level, barrelMatchRecipe.get(), pos, state, barrelBlockEntity);
        } else {
            level.setBlockAndUpdate(pos, state.setValue(BrewingBarrelBlock.PROGRESS, 0));
        }
    }

    protected void cookRecipe(Level level, BrewingBarrelRecipe recipe, BlockPos pos, BlockState state, BrewingBarrelBlockEntity barrelBlockEntity) {
        ++cookingTime;
        getRecipeCookingCompletionTime(recipe);
        if (this.level == null) {
            return;
        }
        if (cookingTime < cookingCompletionTime) {
            return;
        }
        Block block = level.getBlockState(pos).getBlock();
        if (block instanceof BrewingBarrelBlock) {
            if (state.getValue(BrewingBarrelBlock.PROGRESS) < 12){
                level.setBlockAndUpdate(pos, state.setValue(BrewingBarrelBlock.PROGRESS, state.getValue(BrewingBarrelBlock.PROGRESS) + 1));
                barrelBlockEntity.clearCookingTime();
                return;
            }
            ItemStack resultStack = recipe.getResultItem(this.level.registryAccess()).copy();
            ItemStack stackInSlot = itemStackHandler.getStackInSlot(inputSlot);
            if (stackInSlot.isEmpty()) {
                itemStackHandler.setStackInSlot(inputSlot, resultStack);
            } else if (ItemStack.isSameItemSameTags(stackInSlot, resultStack)) {
                stackInSlot.grow(resultStack.getCount());
            }
            for (int i = 0; i < inputSlot; ++i) {
                ItemStack slotStack = itemStackHandler.getStackInSlot(i);
                if (slotStack.hasCraftingRemainingItem()) {
                    AbstractPotBlockEntity.spawnRemainderItem(slotStack.getCraftingRemainingItem(), getBlockState(), worldPosition, level);
                }
                if (!slotStack.isEmpty()){
                    slotStack.shrink(1);
                }
            }
            containerItem = recipe.getContainerItem().getItems()[0];
            clearCookingTime();
            level.setBlockAndUpdate(pos, state.setValue(BrewingBarrelBlock.PROGRESS, 0));
            itemStackHandlerChanged();
        }
    }

    protected boolean canCookRecipe(BrewingBarrelRecipe recipe, RecipeWrapper recipeWrapper) {
        if (!canCooking()) {
            return false;
        }
        if (this.level == null) {
            return false;
        }
        ItemStack resultStack = recipe.getResultItem(this.level.registryAccess());
        if (resultStack.isEmpty()) {
            return false;
        }
        if(!recipe.matches(recipeWrapper, this.level)){
            return false;
        }
        ItemStack stackInSlot = itemStackHandler.getStackInSlot(inputSlot);
        if (stackInSlot.isEmpty()){
            return true;
        }
        if (!ItemStack.isSameItem(stackInSlot, resultStack)) {
            return false;
        }
        return stackInSlot.getCount() + resultStack.getCount() <= itemStackHandler
                .getSlotLimit(inputSlot);
    }

    protected Optional<BrewingBarrelRecipe> getBrewingBarrelMatchRecipe(RecipeWrapper recipeWrapper) {
        return hasInput() && level != null
                ? check.getRecipeFor(recipeWrapper, this.level)
                : Optional.empty();
    }

    public void interact(ItemStack itemStackInHand, Player player, Level level, BlockPos pos){
        if (itemStackInHand.isEmpty() && player.isShiftKeyDown()) {
            getItemStackListInBrewingBarrel(false).forEach(itemStack -> {
                if (!player.getInventory().add(itemStack)) {
                    player.drop(itemStack, false);
                }
            });
            level.playSound(null, pos, SoundEvents.BARREL_CLOSE, SoundSource.BLOCKS, 0.8F, 1.0F);
            itemStackHandlerChanged();
            return;
        }
        if (canOutput(itemStackInHand)) {
            ItemStack outputItem = getOutputItem();
            if (outputItem == null || outputItem.isEmpty()) {
                return;
            }
            ModItemStackHandlerHelper.getOutputItem(itemStackInHand, player, itemStackHandler, inputSlot);
            level.playSound(null, pos, SoundEvents.BARREL_CLOSE, SoundSource.BLOCKS, 0.8F, 1.0F);
            if (player instanceof ServerPlayer serverPlayer) {
                ModAdvancementRegistry.USE_BREWING_BARREL.trigger(serverPlayer);
            }
            itemStackHandlerChanged();
        } else {
            insertItem(itemStackInHand);
            level.playSound(null, pos, SoundEvents.BARREL_OPEN, SoundSource.BLOCKS, 0.8F, 1.0F);
            itemStackHandlerChanged();
        }
    }

    public void insertItem(ItemStack itemStackInHand) {
        ModItemStackHandlerHelper.fillInItem(itemStackHandler, itemStackInHand,0, inputSlot);
        itemStackHandlerChanged();
    }

    @Nullable
    public ItemStack getOutputItem() {
        return itemStackHandler.getStackInSlot(inputSlot);
    }

    public boolean canOutput(ItemStack itemStackInHand) {
        return !containerItem.isEmpty() && ItemStack.isSameItem(containerItem, itemStackInHand);
    }

    protected boolean hasInput() {
        return ModItemStackHandlerHelper.hasInput(itemStackHandler, inputSlot);
    }

    public List<ItemStack> getItemStackListInBrewingBarrel(boolean includeOutput) {
        if (includeOutput){
            return ModItemStackHandlerHelper.getItemStackListInSlot(itemStackHandler, 0, allSlot);
        } else {
            return ModItemStackHandlerHelper.getItemStackListInSlot(itemStackHandler, 0, inputSlot);
        }
    }

    protected boolean canCooking() {
        if (this.level == null) {
            return false;
        }
        return hasInput();
    }

    protected void clearCookingTime(){
        if (cookingTime >= cookingCompletionTime){
            cookingTime = 0;
        }
    }

    public void getRecipeCookingCompletionTime(BrewingBarrelRecipe recipe){
        cookingCompletionTime = recipe.getCookingTime();
    }

    @Override
    public void itemStackHandlerChanged() {
        super.setChanged();
        if (level != null){
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
        }
    }

    @Override
    @Nullable
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void load(@Nonnull CompoundTag compound) {
        super.load(compound);
        itemStackHandler.deserializeNBT(compound.getCompound("ItemStackHandler"));
        containerItem = ItemStack.of(compound.getCompound("Container"));
        cookingTime = compound.getInt("CookingTime");
        cookingCompletionTime = compound.getInt("CookingCompletionTime");
    }

    @Override
    public void saveAdditional(@Nonnull CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putInt("CookingTime", cookingTime);
        compound.putInt("CookingCompletionTime", cookingCompletionTime);
        compound.put("ItemStackHandler", itemStackHandler.serializeNBT());
        compound.put("Container", containerItem.serializeNBT());
    }

    @Override
    public @Nonnull CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        super.saveAdditional(tag);
        tag.put("ItemStackHandler", itemStackHandler.serializeNBT());
        tag.put("Container", containerItem.serializeNBT());
        return tag;
    }

    public int getInputSlot() {
        return inputSlot;
    }

    public int getOutputSlot() {
        return outputSlot;
    }

    public RecipeManager.CachedCheck<RecipeWrapper, BrewingBarrelRecipe> getCheck() {
        return check;
    }

    public ItemStackHandler getItemStackHandler() {
        return itemStackHandler;
    }

    public LazyOptional<IItemHandler> getInputHandler() {
        return inputHandler;
    }

    public LazyOptional<IItemHandler> getOutputHandler() {
        return outputHandler;
    }

    public int getCookingTime() {
        return cookingTime;
    }

    public int getCookingCompletionTime() {
        return cookingCompletionTime;
    }

    public ItemStack getContainerItem() {
        return containerItem;
    }
}
