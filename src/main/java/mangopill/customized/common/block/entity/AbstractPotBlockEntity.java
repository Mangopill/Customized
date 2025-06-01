package mangopill.customized.common.block.entity;

import mangopill.customized.common.block.handler.PotFluidHandler;
import mangopill.customized.common.block.handler.PotItemHandler;
import mangopill.customized.common.block.state.PotState;
import mangopill.customized.common.recipe.AbstractPotRecipe;
import mangopill.customized.common.registry.ModAdvancementRegistry;
import mangopill.customized.common.tag.ModTag;
import mangopill.customized.common.util.CreateItemStackHandler;
import mangopill.customized.common.util.ModItemStackHandlerHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

import static mangopill.customized.common.CustomizedConfig.*;
import static mangopill.customized.common.block.AbstractPotBlock.*;

public abstract class AbstractPotBlockEntity extends BlockEntity implements CreateItemStackHandler {
    private final int ingredientInput;
    private final int seasoningInput;
    private static final int SPICE_INPUT = 1;
    private static final int OUTPUT = 1;
    private final int allSlot;
    private final ItemStackHandler itemStackHandler;
    private final LazyOptional<IItemHandler> inputHandler;
    private final LazyOptional<IItemHandler> outputHandler;
    private final LazyOptional<IFluidHandler> fluidHandler;
    private final RecipeManager.CachedCheck<RecipeWrapper,? extends AbstractPotRecipe> potCheck;
    private final RecipeManager.CachedCheck<Container, CampfireCookingRecipe> campfireCheck;
    private int cookingTime;
    private int cookingCompletionTime;

    public AbstractPotBlockEntity(BlockEntityType<? extends AbstractPotBlockEntity> type, BlockPos pos, BlockState blockState,
                                  int ingredientCount, int seasoningCount, RecipeManager.CachedCheck<RecipeWrapper, ? extends AbstractPotRecipe> potCheck) {
        super(type, pos, blockState);
        ingredientInput = ingredientCount;
        seasoningInput = seasoningCount;
        this.allSlot = ingredientInput + seasoningInput + SPICE_INPUT + OUTPUT;
        this.campfireCheck = RecipeManager.createCheck(RecipeType.CAMPFIRE_COOKING);
        this.itemStackHandler = createItemStackHandler(allSlot);
        this.inputHandler = LazyOptional.of(() ->  new PotItemHandler(itemStackHandler, Direction.UP, ingredientCount, seasoningCount));
        this.outputHandler = LazyOptional.of(() ->  new PotItemHandler(itemStackHandler, Direction.DOWN, ingredientCount, seasoningCount));
        this.fluidHandler = LazyOptional.of(() ->  new PotFluidHandler(level, pos));
        this.potCheck = potCheck;
    }

    @Override
    public <T> @Nonnull LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            if (side == Direction.UP) {
                return inputHandler.cast();
            }
            return outputHandler.cast();
        }
        if (cap == ForgeCapabilities.FLUID_HANDLER) {
            return fluidHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void itemStackHandlerChanged() {
        super.setChanged();
        if (level != null){
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
        }
    }

    public static void cookingTick(Level level, BlockPos pos, BlockState state, AbstractPotBlockEntity potBlockEntity) {
        RecipeWrapper wrapper = new RecipeWrapper(potBlockEntity.itemStackHandler);
        Optional<? extends AbstractPotRecipe> potMatchRecipe = potBlockEntity.getPotMatchRecipe(wrapper);
        if (!potBlockEntity.isHeated(level, pos)){
            potBlockEntity.clearCookingTime();
            return;
        }
        if (potMatchRecipe.isPresent() && potBlockEntity.canCookRecipe(potMatchRecipe.get(),wrapper)){
            //recipe
            if (!RECIPE_COOKING.get()){
                return;
            }
            potBlockEntity.cookRecipe(potMatchRecipe.get(), pos, state);
        } else {
            //customized
            if (!CUSTOM_COOKING.get()){
                return;
            }
            potBlockEntity.cookCustomized(level, pos, state);
        }
    }

    public static void animationTick(Level level, BlockPos pos, BlockState state, AbstractPotBlockEntity potBlockEntity) {
        if (potBlockEntity.isHeated(level, pos)) {
            potBlockEntity.particleTick(level, pos, potBlockEntity);
        }
    }

    abstract public void particleTick(Level level, BlockPos pos, AbstractPotBlockEntity potBlockEntity);

    protected Optional<? extends AbstractPotRecipe> getPotMatchRecipe(RecipeWrapper recipeWrapper) {
        return hasInput() && level != null
                ? potCheck.getRecipeFor(recipeWrapper, this.level)
                : Optional.empty();
    }

    protected Optional<CampfireCookingRecipe> getCampfireMatchRecipe(ItemStack stack) {
        return hasInput() && level != null ? campfireCheck.getRecipeFor(new SimpleContainer(stack), this.level) : Optional.empty();
    }

    protected boolean hasInput() {
        return ModItemStackHandlerHelper.hasInput(itemStackHandler, ingredientInput + seasoningInput + SPICE_INPUT);
    }

    protected boolean canCooking() {
        if (this.level == null) {
            return false;
        }
        return hasInput();
    }

    protected boolean canCookRecipe(AbstractPotRecipe recipe, RecipeWrapper recipeWrapper) {
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
        ItemStack stackInSlot = itemStackHandler.getStackInSlot(ingredientInput + seasoningInput + SPICE_INPUT);
        if (stackInSlot.isEmpty()){
            return true;
        }
        if (!ItemStack.isSameItem(stackInSlot, resultStack)) {
            return false;
        }
        return stackInSlot.getCount() + resultStack.getCount() <= itemStackHandler
                .getSlotLimit(ingredientInput + seasoningInput + SPICE_INPUT);
    }

    protected <T extends AbstractPotRecipe> void cookRecipe(T recipe, BlockPos pos, BlockState state) {
        ++cookingTime;
        getRecipeCookingCompletionTime(recipe);
        lidAccelerate(state);
        if (cookingTime < cookingCompletionTime) {
            return;
        }
        if (this.level == null) {
            return;
        }
        ItemStack resultStack = recipe.getResultItem(this.level.registryAccess()).copy();
        ItemStack stackInSlot = itemStackHandler.getStackInSlot(ingredientInput + seasoningInput + SPICE_INPUT);
        if (stackInSlot.isEmpty()) {
            itemStackHandler.setStackInSlot(ingredientInput + seasoningInput + SPICE_INPUT, resultStack);
        } else if (ItemStack.isSameItem(stackInSlot, resultStack)) {
            stackInSlot.grow(resultStack.getCount());
        }
        for (int i = 0; i < ingredientInput + seasoningInput + SPICE_INPUT; ++i) {
            ItemStack slotStack = itemStackHandler.getStackInSlot(i);
            if (slotStack.hasCraftingRemainingItem()) {
                spawnRemainderItem(slotStack.getCraftingRemainingItem(), getBlockState(), worldPosition, level);
            }
            if (!slotStack.isEmpty()){
                slotStack.shrink(1);
            }
        }
        clearCookingTime();
        itemStackHandlerChanged();
    }

    protected void cookCustomized(Level level, BlockPos pos, BlockState state) {
        for (int i = 0; i < ingredientInput; ++i) {
            ItemStack stackInSlot = itemStackHandler.getStackInSlot(i);
            Optional<CampfireCookingRecipe> recipe = getCampfireMatchRecipe(stackInSlot);
            if (recipe.isEmpty()) {
                continue;
            }
            ++cookingTime;
            getCustomizedCookingCompletionTime();
            lidAccelerate(state);
            if (cookingTime < cookingCompletionTime) {
                return;
            }
            ItemStack resultStack = recipe.get().assemble(new SimpleContainer(stackInSlot), level.registryAccess()).copy();
            resultStack.setCount(stackInSlot.getCount());
            itemStackHandler.setStackInSlot(i, resultStack);
        }
        clearCookingTime();
        itemStackHandlerChanged();
    }

    public void takeOutItem(Level level, BlockState state, BlockPos pos) {
        Direction direction = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
        double x = pos.getX() + 0.5 + (direction.getStepX() * 0.25);
        double y = pos.getY() + 0.7;
        double z = pos.getZ() + 0.5 + (direction.getStepZ() * 0.25);
        for (int i = 0; i < ingredientInput + seasoningInput + SPICE_INPUT; i++) {
            ItemStack stackInSlot = getItemStackHandler().getStackInSlot(i);
            if (stackInSlot.isEmpty()){
                continue;
            }
            ItemEntity itemEntity = new ItemEntity(level, x, y, z, stackInSlot.copy());
            itemEntity.setDeltaMovement(direction.getStepX() * -0.1F, 0.3F, direction.getStepZ() * -0.1F);
            level.addFreshEntity(itemEntity);
            stackInSlot.copyAndClear();
        }
        itemStackHandlerChanged();
    }

    public void insertItem(ItemStack itemStackInHand) {
        ModItemStackHandlerHelper.insertItem(itemStackInHand, itemStackHandler, ingredientInput, seasoningInput, SPICE_INPUT);
        itemStackHandlerChanged();
    }

    public void getCustomizedCookingCompletionTime(){
        int time = 100;
        for (int i = 0; i < ingredientInput; ++i) {
            time += itemStackHandler.getStackInSlot(i).getCount() * 20;
        }
        cookingCompletionTime = time;
    }

    public <T extends AbstractPotRecipe> void getRecipeCookingCompletionTime(T holder){
        cookingCompletionTime = holder.getCookingTime();
    }

    protected static void spawnRemainderItem(ItemStack remainderStack, BlockState state, BlockPos pos, Level level) {
        Direction direction = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
        double x = pos.getX() + 0.5 + (direction.getStepX() * 0.25);
        double y = pos.getY() + 0.7;
        double z = pos.getZ() + 0.5 + (direction.getStepZ() * 0.25);
        if (level != null) {
            ItemEntity entity = new ItemEntity(level, x, y, z, remainderStack);
            entity.setDeltaMovement(direction.getStepX() * 0.1F, 0.3F, direction.getStepZ() * 0.1F);
            level.addFreshEntity(entity);
        }
    }

    public boolean isHeated() {
        if (level == null){
            return false;
        }
        return this.isHeated(level, worldPosition);
    }

    protected boolean isHeated(Level level, BlockPos pos) {
        BlockState stateBelow = level.getBlockState(pos.below());
        if (!stateBelow.is(ModTag.HEAT_SOURCE)) {
            return false;
        }
        if (stateBelow.hasProperty(BlockStateProperties.LIT)){
            return stateBelow.getValue(BlockStateProperties.LIT);
        }
        return true;
    }

    protected void clearCookingTime(){
        if (cookingTime != 0){
            cookingTime = 0;
        }
    }
    //speed up
    public void stirFryAccelerate(ItemStack itemStackInHand, LivingEntity entity, InteractionHand hand){
        if (!itemStackInHand.is(ModTag.SPATULA) || cookingTime == 0) {
            return;
        }
        cookingTime += 10;
        if (entity instanceof Player player) {
            itemStackInHand.hurtAndBreak(1, player, (p) -> player.broadcastBreakEvent(hand));
        }
    }

    public void lidAccelerate(BlockState state){
        if (state.getValue(LID).equals(PotState.WITH_LID)){
            ++cookingTime;
        }
    }

    public List<ItemStack> getItemStackListInPot(boolean includeOutput, boolean includeSeasoningAndSpice) {
        if (includeOutput){
            if (includeSeasoningAndSpice){
                return ModItemStackHandlerHelper.getItemStackListInSlot(itemStackHandler, 0, allSlot);
            } else {
                List<ItemStack> stackList = ModItemStackHandlerHelper.getItemStackListInSlot(itemStackHandler, 0, ingredientInput);
                stackList.addAll(ModItemStackHandlerHelper.getItemStackListInSlot(itemStackHandler, ingredientInput + seasoningInput + SPICE_INPUT, allSlot));
                return stackList;
            }
        } else {
            if (includeSeasoningAndSpice){
                return ModItemStackHandlerHelper.getItemStackListInSlot(itemStackHandler, 0, ingredientInput + seasoningInput + SPICE_INPUT);
            } else {
                return ModItemStackHandlerHelper.getItemStackListInSlot(itemStackHandler, 0, ingredientInput);
            }
        }
    }

    public void getOutputInPot(ItemStack itemStackInHand, Player player){
        ModItemStackHandlerHelper.getOutputItem(itemStackInHand, player, itemStackHandler, ingredientInput + seasoningInput + SPICE_INPUT);
        if (player instanceof ServerPlayer serverPlayer) {
            ModAdvancementRegistry.GET_FAMOUS_DISH.trigger(serverPlayer);
        }
        itemStackHandlerChanged();
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
        cookingTime = compound.getInt("CookingTime");
        cookingCompletionTime = compound.getInt("CookingCompletionTime");
    }

    @Override
    public void saveAdditional(@Nonnull CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putInt("CookingTime", cookingTime);
        compound.putInt("CookingCompletionTime", cookingCompletionTime);
        compound.put("ItemStackHandler", itemStackHandler.serializeNBT());
    }

    @Override
    public @Nonnull CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        super.saveAdditional(tag);
        tag.put("ItemStackHandler", itemStackHandler.serializeNBT());
        return tag;
    }

    //getter
    public int getIngredientInput() {
        return ingredientInput;
    }

    public int getSeasoningInput() {
        return seasoningInput;
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

    public LazyOptional<IFluidHandler> getFluidHandler() {
        return fluidHandler;
    }

    public RecipeManager.CachedCheck<RecipeWrapper, ? extends AbstractPotRecipe> getPotCheck() {
        return potCheck;
    }

    public RecipeManager.CachedCheck<Container, CampfireCookingRecipe> getCampfireCheck() {
        return campfireCheck;
    }
}
