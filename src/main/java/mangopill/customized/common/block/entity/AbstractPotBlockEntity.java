package mangopill.customized.common.block.entity;

import mangopill.customized.common.block.handler.*;
import mangopill.customized.common.block.record.PotRecord;
import mangopill.customized.common.block.state.PotState;
import mangopill.customized.common.item.AbstractPlateItem;
import mangopill.customized.common.recipe.AbstractPotRecipe;
import mangopill.customized.common.registry.CAdvancementRegistry;
import mangopill.customized.common.tag.ModTag;
import mangopill.customized.common.util.*;
import mangopill.customized.common.util.record.UUIDRecord;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.core.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;

import javax.annotation.Nullable;
import java.util.*;

import static mangopill.customized.common.CustomizedConfig.*;
import static mangopill.customized.common.block.AbstractPotBlock.*;
import static mangopill.customized.common.util.CItemStackHandlerHelper.*;
import static mangopill.customized.common.util.CItemStackHandlerHelper.getConsumptionCount;
import static mangopill.customized.common.util.CStringUtil.*;
import static mangopill.customized.common.util.InteractUtil.*;
import static mangopill.customized.common.util.PropertyValueUtil.*;
import static mangopill.customized.common.util.RecipeUtil.*;
import static mangopill.customized.common.util.component.CItemMatchMode.*;
import static mangopill.customized.common.util.component.CompoundTagHelper.*;
import static mangopill.customized.common.util.component.ItemComponentUtil.*;

public abstract class AbstractPotBlockEntity extends BlockEntity implements CreateItemStackHandler {
    protected final int ingredientInput;
    protected final int seasoningInput;
    protected final int spiceInput;
    // Container Output
    public static final int OUTPUT = 1;
    private final boolean canInputDrive;
    protected final int allSlot;
    protected final ItemStackHandler itemStackHandler;
    private final PotItemHandler inputAndOutputHandler;
    protected final PotFluidHandler fluidHandler;
    protected final AbstractPlateItem plateItem;
    protected Ingredient containerItem;
    protected final RecipeManager.CachedCheck<RecipeWrapper, ? extends AbstractPotRecipe> potCheck;
    protected final RecipeManager.CachedCheck<SingleRecipeInput, CampfireCookingRecipe> campfireCheck;
    protected UUID lastInteractPlayerId;
    protected int cookingTime;
    protected int cookingCompletionTime;
    protected int customizedTime;
    protected int customizedCompletionTime;

    protected AbstractPotBlockEntity(BlockEntityType<? extends AbstractPotBlockEntity> type, BlockPos pos, BlockState blockState,
                                     int ingredientCount, int seasoningCount, int spiceCount,
                                     RecipeManager.CachedCheck<RecipeWrapper, ? extends AbstractPotRecipe> potCheck,
                                     AbstractPlateItem plateItem, boolean canInputDrive) {
        super(type, pos, blockState);
        this.ingredientInput = ingredientCount;
        this.seasoningInput = seasoningCount;
        this.spiceInput = spiceCount;
        this.canInputDrive = canInputDrive;
        this.plateItem = plateItem;
        this.allSlot = ingredientInput + seasoningInput + spiceInput + OUTPUT;
        this.campfireCheck = RecipeManager.createCheck(RecipeType.CAMPFIRE_COOKING);
        this.itemStackHandler = createItemStackHandler(allSlot);
        this.inputAndOutputHandler = new PotItemHandler(this, itemStackHandler);
        this.fluidHandler = new PotFluidHandler(this, FluidType.BUCKET_VOLUME);
        this.containerItem = Ingredient.EMPTY;
        this.potCheck = potCheck;
        this.lastInteractPlayerId = UUIDRecord.EMPTY.uuid();
    }

    protected AbstractPotBlockEntity(BlockPos pos, BlockState blockState, PotRecord record) {
        this(record.entityType(), pos, blockState, record.ingredientCount(), record.seasoningCount(), record.spiceCount(), record.potCheck(), record.plateItem().get(), record.canInputDrive());
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
        if (!potBlockEntity.hasInput() || potBlockEntity.level == null || (potBlockEntity.isCanInputDrive() && potBlockEntity.getFluidHandler().isEmpty())){
            potBlockEntity.clearCookingTime();
            potBlockEntity.clearCustomizedTime();
            return;
        }
        if (potMatchRecipe.isPresent() && potBlockEntity.canCookRecipe(potMatchRecipe.get(),wrapper)){
            if (!RECIPE_COOKING.get()) return;
            potBlockEntity.cookRecipe(potMatchRecipe.get(), pos, state);
        } else if (potBlockEntity.isHeated()){
            potBlockEntity.cookCampfire(level, state);
            if (!CUSTOM_COOKING.get()) return;
            potBlockEntity.cookCustomized(level, state);
        }
        potBlockEntity.itemStackHandlerChanged();
    }

    public static void animationTick(Level level, BlockPos pos, BlockState state, AbstractPotBlockEntity potBlockEntity) {
        if (potBlockEntity.isHeated()) {
            potBlockEntity.particleTick(level, pos, potBlockEntity);
        }
    }

    abstract public void particleTick(Level level, BlockPos pos, AbstractPotBlockEntity potBlockEntity);

    protected Optional<? extends AbstractPotRecipe> getPotMatchRecipe(RecipeWrapper recipeWrapper) {
        return hasInput() ? getCheckRecipeOptionalFor(potCheck, recipeWrapper, level) : Optional.empty();
    }

    protected Optional<CampfireCookingRecipe> getCampfireMatchRecipe(ItemStack stack) {
        return hasInput() ? getCheckRecipeOptionalFor(campfireCheck, new SingleRecipeInput(stack), level) : Optional.empty();
    }

    public boolean hasInput() {
        return CItemStackHandlerHelper.hasInput(itemStackHandler, ingredientInput + seasoningInput + spiceInput);
    }

    protected boolean canCookRecipe(AbstractPotRecipe recipe, RecipeWrapper recipeWrapper) {
        assert level != null;
        ItemStack resultStack = recipe.getResultItem(level.registryAccess());
        containerItem = recipe.getContainerItem();
        if (resultStack.isEmpty()) return false;
        if (!recipe.matches(recipeWrapper, level)) return false;
        if (recipe.isHeated() && !isHeated()) return false;
        if (!fluidHandler.isEmpty() && !recipe.getFluidIngredient().test(fluidHandler.getStoredFluid())) return false;
        ItemStack stackInSlot = itemStackHandler.getStackInSlot(ingredientInput + seasoningInput + spiceInput);
        return stackInSlot.getCount() + resultStack.getCount() <= itemStackHandler.getSlotLimit(ingredientInput + seasoningInput + spiceInput);
    }

    protected void cookRecipe(AbstractPotRecipe recipe, BlockPos pos, BlockState state) {
        ++cookingTime;
        getRecipeCookingCompletionTime(recipe);
        lidAccelerate(state);
        if (cookingTime < cookingCompletionTime) return;
        if (!containerItem.test(itemStackHandler.getStackInSlot(ingredientInput + seasoningInput + spiceInput))) return;
        assert level != null;
        ItemStack resultStack = recipe.getResultItem(level.registryAccess()).copy();
        spawnItemEntity(level, resultStack.copy(), state, pos);
        for (int i = 0; i < ingredientInput + seasoningInput + spiceInput + OUTPUT; ++i) {
            ItemStack slotStack = itemStackHandler.getStackInSlot(i);
            spawnUsingConvertsTo(level, List.of(slotStack), state, pos);
            shrinkItemStack(slotStack, 1);
        }
        if (level.getPlayerByUUID(lastInteractPlayerId) instanceof ServerPlayer serverPlayer) {
            CAdvancementRegistry.GET_FAMOUS_DISH.get().trigger(serverPlayer);
        }
        containerItem = Ingredient.EMPTY;
        clearCookingTime();
    }

    protected void cookCampfire(Level level, BlockState state) {
        for (int i = 0; i < ingredientInput; ++i) {
            ItemStack stackInSlot = itemStackHandler.getStackInSlot(i);
            Optional<CampfireCookingRecipe> recipe = getCampfireMatchRecipe(stackInSlot);
            if (recipe.isEmpty()) continue;
            ++cookingTime;
            getCampfireCookingCompletionTime();
            lidAccelerate(state);
            if (cookingTime < cookingCompletionTime) return;
            ItemStack resultStack = recipe.get().assemble(new SingleRecipeInput(stackInSlot), level.registryAccess()).copyWithCount(stackInSlot.getCount());
            itemStackHandler.setStackInSlot(i, resultStack);
        }
        clearCookingTime();
    }

    protected void cookCustomized(Level level, BlockState state) {
        ++customizedTime;
        lidAccelerate(state);
        getCustomizedCookingCompletionTime();
        if (customizedTime < customizedCompletionTime || itemStackHandler.getStackInSlot(ingredientInput + seasoningInput + spiceInput).isEmpty()) return;
        transferAndSpawn(level, getItemStackListInPot(false, true));
        clearCustomizedTime();
    }

    protected void transferAndSpawn(Level level, List<ItemStack> stackList) {
        ItemStack outputItem = itemStackHandler.getStackInSlot(ingredientInput + seasoningInput + spiceInput);
        if (simpleTest(outputItem, plateItem, SAME_ITEM) && (outputItem.getItem() instanceof AbstractPlateItem plate)) {
            ItemStackHandler newItemStackHandler = plate.copyItemStackHandlerByComponent(outputItem);
            spawnUsingConvertsTo(level, stackList, getBlockState(), getBlockPos());
            stackList.forEach(itemStack -> plate.insertItem(itemStack, newItemStackHandler));
            List<ItemStack> newStackList = getItemStackListInSlot(newItemStackHandler, 0, newItemStackHandler.getSlots());
            updatePlateAll(outputItem, newItemStackHandler, copyItemStackHandler(newItemStackHandler, true),
                    getFoodPropertyByPropertyValue(level, newStackList, getBlockState().getBlock(), true),
                    getConsumptionCount(newStackList), getConsumptionCount(newStackList),
                    lastInteractPlayerId, getProgress(level));
            spawnItemEntity(level, outputItem, getBlockState(), getBlockPos());
            lastInteractPlayerId = UUIDRecord.EMPTY.uuid();
        }
    }

    public void takeOutItem(Level level, BlockState state, BlockPos pos) {
        spawnItemEntityList(level, getItemStackListInPot(true, true), state, pos);
        itemStackHandlerChanged();
    }

    public void insertItem(ItemStack itemStackInHand, Player player) {
        CItemStackHandlerHelper.insertItem(itemStackInHand, itemStackHandler, ingredientInput, seasoningInput, spiceInput, OUTPUT, containerItem);
        lastInteractPlayerId = player.getUUID();
        itemStackHandlerChanged();
    }

    public void getCampfireCookingCompletionTime(){
        cookingCompletionTime = getTotalItemCount(getItemStackListInPot(false, false)) * 20;
    }

    public void getRecipeCookingCompletionTime(AbstractPotRecipe recipe){
        cookingCompletionTime = recipe.getCookingTime();
    }

    public void getCustomizedCookingCompletionTime(){
        customizedCompletionTime = getTotalItemCount(getItemStackListInPot(false, true)) * 10;
    }

    public boolean isHeated() {
        if (level == null) return false;
        BlockState stateBelow = level.getBlockState(worldPosition.below());
        if (!stateBelow.is(ModTag.HEAT_SOURCE)) return false;
        if (stateBelow.hasProperty(BlockStateProperties.LIT)) return stateBelow.getValue(BlockStateProperties.LIT);
        if (stateBelow.hasProperty(LID)) return !stateBelow.getValue(LID).equals(PotState.WITHOUT_LID);
        return true;
    }

    protected void clearCookingTime(){
        cookingTime = 0;
        cookingCompletionTime = 0;
    }

    protected void clearCustomizedTime(){
        customizedTime = 0;
        customizedCompletionTime = 0;
    }

    // speed up
    public void stirFryAccelerate(ItemStack itemStackInHand, Player player, ItemStack spatula){
        if (!simpleTest(itemStackInHand, spatula, SAME_ITEM)) return;
        if (cookingTime > 0) {
            cookingTime += 10;
        }
        if (customizedTime > 0) {
            customizedTime += 10;
        }
        hurtAndBreakItemStack(itemStackInHand, player, 1);
        shuffleItemStackHandlerInRange(itemStackHandler, 0, ingredientInput);
    }

    public void lidAccelerate(BlockState state){
        if (state.getValue(LID).equals(PotState.WITH_LID)){
            ++cookingTime;
            ++customizedTime;
        }
    }

    public List<ItemStack> getItemStackListInPot(boolean includeOutput, boolean includeSeasoningAndSpice) {
        if (includeOutput){
            if (includeSeasoningAndSpice){
                return CItemStackHandlerHelper.getItemStackListInSlot(itemStackHandler, 0, allSlot);
            } else {
                List<ItemStack> stackList = CItemStackHandlerHelper.getItemStackListInSlot(itemStackHandler, 0, ingredientInput);
                stackList.addAll(CItemStackHandlerHelper.getItemStackListInSlot(itemStackHandler, ingredientInput + seasoningInput + spiceInput, allSlot));
                return stackList;
            }
        } else {
            if (includeSeasoningAndSpice){
                return CItemStackHandlerHelper.getItemStackListInSlot(itemStackHandler, 0, ingredientInput + seasoningInput + spiceInput);
            } else {
                return CItemStackHandlerHelper.getItemStackListInSlot(itemStackHandler, 0, ingredientInput);
            }
        }
    }

    public Boolean getProgress(Level level) {
        if (level.getPlayerByUUID(lastInteractPlayerId) instanceof ServerPlayer serverPlayer) {
            AdvancementHolder advancement = serverPlayer.server.getAdvancements().get(getCLoc("master_of_culinary_arts"));
            return advancement != null && serverPlayer.getAdvancements().getOrStartProgress(advancement).hasProgress();
        }
        return false;
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
        if (compound.contains("Container")) {
            containerItem = deserializeIngredientNBT(compound);
        }
        cookingTime = compound.getInt("CookingTime");
        cookingCompletionTime = compound.getInt("CookingCompletionTime");
        customizedTime = compound.getInt("CustomizedTime");
        customizedCompletionTime = compound.getInt("CustomizedCompletionTime");
        lastInteractPlayerId = compound.getUUID("LastInteractPlayerId");
        if (compound.contains("FluidHandler")) {
            deserializeFluidHandlerTag(compound, registries, fluidHandler);
        }
    }

    @Override
    public void saveAdditional(CompoundTag compound, HolderLookup.Provider registries) {
        super.saveAdditional(compound, registries);
        compound.putInt("CookingTime", cookingTime);
        compound.putInt("CookingCompletionTime", cookingCompletionTime);
        compound.putInt("CustomizedTime", customizedTime);
        compound.putInt("CustomizedCompletionTime", customizedCompletionTime);
        compound.put("ItemStackHandler", itemStackHandler.serializeNBT(registries));
        putFluidHandlerTag(compound, registries, fluidHandler);
        putIngredientTag(compound, containerItem);
        compound.putUUID("LastInteractPlayerId", lastInteractPlayerId);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag, registries);
        return tag;
    }

    public int getIngredientInput() {
        return ingredientInput;
    }

    public int getSeasoningInput() {
        return seasoningInput;
    }

    public int getSpiceInput() {
        return spiceInput;
    }

    public Ingredient getContainerItem() {
        return containerItem;
    }

    public ItemStackHandler getItemStackHandler() {
        return itemStackHandler;
    }

    public PotItemHandler getInputAndOutputHandler() {
        return inputAndOutputHandler;
    }

    public PotFluidHandler getFluidHandler() {
        return fluidHandler;
    }

    public RecipeManager.CachedCheck<RecipeWrapper, ? extends AbstractPotRecipe> getPotCheck() {
        return potCheck;
    }

    public RecipeManager.CachedCheck<SingleRecipeInput, CampfireCookingRecipe> getCampfireCheck() {
        return campfireCheck;
    }

    public UUID getLastInteractPlayerId() {
        return lastInteractPlayerId;
    }

    public int getCookingTime() {
        return cookingTime;
    }

    public int getCookingCompletionTime() {
        return cookingCompletionTime;
    }

    public int getCustomizedTime() {
        return customizedTime;
    }

    public int getCustomizedCompletionTime() {
        return customizedCompletionTime;
    }

    public boolean isCanInputDrive() {
        return canInputDrive;
    }
}
