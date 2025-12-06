package mangopill.customized.common.block.entity;

import mangopill.customized.common.block.handler.PotFluidHandler;
import mangopill.customized.common.block.handler.PotItemHandler;
import mangopill.customized.common.block.state.PotState;
import mangopill.customized.common.item.AbstractPlateItem;
import mangopill.customized.common.recipe.AbstractPotRecipe;
import mangopill.customized.common.registry.CAdvancementRegistry;
import mangopill.customized.common.tag.ModTag;
import mangopill.customized.common.util.CreateItemStackHandler;
import mangopill.customized.common.util.CItemStackHandlerHelper;
import mangopill.customized.common.util.record.UUIDRecord;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;

import javax.annotation.Nullable;
import java.util.*;

import static mangopill.customized.common.CustomizedConfig.*;
import static mangopill.customized.common.block.AbstractPotBlock.*;
import static mangopill.customized.common.util.CompoundTagHelper.*;
import static mangopill.customized.common.util.CItemStackHandlerHelper.*;
import static mangopill.customized.common.util.StringUtil.*;
import static mangopill.customized.common.util.component.PlateComponentUtil.*;
import static mangopill.customized.common.util.PropertyValueUtil.*;

public abstract class AbstractPotBlockEntity extends BlockEntity implements CreateItemStackHandler {
    private final int ingredientInput;
    private final int seasoningInput;
    private final int spiceInput;
    public static final int OUTPUT = 1;
    private final int allSlot;
    private final ItemStackHandler itemStackHandler;
    private final PotItemHandler inputAndOutputHandler;
    private final PotFluidHandler fluidHandler;
    private Ingredient containerItem;
    private final RecipeManager.CachedCheck<RecipeWrapper,? extends AbstractPotRecipe> potCheck;
    private final RecipeManager.CachedCheck<SingleRecipeInput, CampfireCookingRecipe> campfireCheck;
    private UUID lastInteractPlayerId;
    private int cookingTime;
    private int cookingCompletionTime;
    private int customizedTime;
    private int customizedCompletionTime;

    protected AbstractPotBlockEntity(BlockEntityType<? extends AbstractPotBlockEntity> type, BlockPos pos, BlockState blockState,
                                  int ingredientCount, int seasoningCount, int spiceCount,
                                  RecipeManager.CachedCheck<RecipeWrapper, ? extends AbstractPotRecipe> potCheck) {
        super(type, pos, blockState);
        this.ingredientInput = ingredientCount;
        this.seasoningInput = seasoningCount;
        this.spiceInput = spiceCount;
        this.allSlot = ingredientInput + seasoningInput + spiceInput + OUTPUT;
        this.campfireCheck = RecipeManager.createCheck(RecipeType.CAMPFIRE_COOKING);
        this.itemStackHandler = createItemStackHandler(allSlot);
        this.inputAndOutputHandler = new PotItemHandler(this, itemStackHandler);
        this.fluidHandler = new PotFluidHandler(this, FluidType.BUCKET_VOLUME);
        this.containerItem = Ingredient.EMPTY;
        this.potCheck = potCheck;
        this.lastInteractPlayerId = UUIDRecord.NULL.uuid();
    }

    @Override
    public void itemStackHandlerChanged() {
        super.setChanged();
        if (level != null){
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
        }
    }

    abstract public AbstractPlateItem getPlateItem();

    public static void cookingTick(Level level, BlockPos pos, BlockState state, AbstractPotBlockEntity potBlockEntity) {
        RecipeWrapper wrapper = new RecipeWrapper(potBlockEntity.itemStackHandler);
        Optional<RecipeHolder<? extends AbstractPotRecipe>> potMatchRecipe = potBlockEntity.getPotMatchRecipe(wrapper);
        if (!potBlockEntity.hasInput() || potBlockEntity.level == null){
            potBlockEntity.clearCookingTime();
            potBlockEntity.clearCustomizedTime();
            return;
        }
        if (potMatchRecipe.isPresent() && potBlockEntity.canCookRecipe(potMatchRecipe.get().value(),wrapper)){
            if (!RECIPE_COOKING.get()){
                return;
            }
            potBlockEntity.cookRecipe(potMatchRecipe.get(), pos, state);
        } else if (potBlockEntity.isHeated()){
            potBlockEntity.cookCampfire(level, state);
            if (!CUSTOM_COOKING.get()){
                return;
            }
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

    public static void addSimpleParticle(Level level, BlockPos pos, ParticleOptions type, float probability,
                                         float xMin, float xMax, float yOffset, float zMin, float zMax) {
        RandomSource random = RandomSource.create();
        if (random.nextFloat() < probability) {
            double x = pos.getX() + Math.clamp(random.nextDouble(), xMin, xMax);
            double y = pos.getY() + yOffset;
            double z = pos.getZ() + Math.clamp(random.nextDouble(), zMin, zMax);
            level.addParticle(type, x, y, z, 0.0D, 0.0D, 0.0D);
        }
    }

    protected Optional<RecipeHolder<? extends AbstractPotRecipe>> getPotMatchRecipe(RecipeWrapper recipeWrapper) {
        return hasInput() && level != null
                ? potCheck.getRecipeFor(recipeWrapper, this.level).map(holder -> (RecipeHolder<? extends AbstractPotRecipe>) holder)
                : Optional.empty();
    }

    protected Optional<RecipeHolder<CampfireCookingRecipe>> getCampfireMatchRecipe(ItemStack stack) {
        return hasInput() && level != null ? campfireCheck.getRecipeFor(new SingleRecipeInput(stack), this.level) : Optional.empty();
    }

    public boolean hasInput() {
        return CItemStackHandlerHelper.hasInput(itemStackHandler, ingredientInput + seasoningInput + spiceInput);
    }

    protected boolean canCookRecipe(AbstractPotRecipe recipe, RecipeWrapper recipeWrapper) {
        ItemStack resultStack = recipe.getResultItem(this.level.registryAccess());
        containerItem = recipe.getContainerItem();
        if (resultStack.isEmpty()) {
            return false;
        }
        if(!recipe.matches(recipeWrapper, this.level)){
            return false;
        }
        if (recipe.isHeated() && !isHeated()) {
            return false;
        }
        if(!fluidHandler.isEmpty() && !recipe.getFluidIngredient().test(fluidHandler.getStoredFluid())){
            return false;
        }
        ItemStack stackInSlot = itemStackHandler.getStackInSlot(ingredientInput + seasoningInput + spiceInput);
        return stackInSlot.getCount() + resultStack.getCount() <= itemStackHandler.getSlotLimit(ingredientInput + seasoningInput + spiceInput);
    }

    protected void cookRecipe(RecipeHolder<? extends AbstractPotRecipe> holder, BlockPos pos, BlockState state) {
        ++cookingTime;
        getRecipeCookingCompletionTime(holder);
        lidAccelerate(state);
        if (cookingTime < cookingCompletionTime) {
            return;
        }
        if (!containsSameItem(List.of(containerItem.getItems()), itemStackHandler.getStackInSlot(ingredientInput + seasoningInput + spiceInput))) {
            return;
        }
        ItemStack resultStack = holder.value().getResultItem(this.level.registryAccess()).copy();
        spawnItemEntity(level, resultStack.copy(), state, pos);
        for (int i = 0; i < ingredientInput + seasoningInput + spiceInput + OUTPUT; ++i) {
            ItemStack slotStack = itemStackHandler.getStackInSlot(i);
            spawnUsingConvertsTo(level, List.of(slotStack), state, pos);
            if (!slotStack.isEmpty()){
                slotStack.shrink(1);
            }
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
            Optional<RecipeHolder<CampfireCookingRecipe>> recipe = getCampfireMatchRecipe(stackInSlot);
            if (recipe.isEmpty()) {
                continue;
            }
            ++cookingTime;
            getCampfireCookingCompletionTime();
            lidAccelerate(state);
            if (cookingTime < cookingCompletionTime) {
                return;
            }
            ItemStack resultStack = recipe.get().value().assemble(new SingleRecipeInput(stackInSlot), level.registryAccess()).copy();
            resultStack.setCount(stackInSlot.getCount());
            itemStackHandler.setStackInSlot(i, resultStack);
        }
        clearCookingTime();
    }

    protected void cookCustomized(Level level, BlockState state) {
        ++customizedTime;
        lidAccelerate(state);
        getCustomizedCookingCompletionTime();
        if (customizedTime < customizedCompletionTime || itemStackHandler.getStackInSlot(ingredientInput + seasoningInput + spiceInput).isEmpty()) {
            return;
        }
        transferAndSpawn(level);
        clearCustomizedTime();
    }

    protected void transferAndSpawn(Level level) {
        List<ItemStack> stackList = this.getItemStackListInPot(false, true);
        ItemStack outputItem = itemStackHandler.getStackInSlot(ingredientInput + seasoningInput + spiceInput);
        if (outputItem.is(getPlateItem()) && outputItem.getItem() instanceof AbstractPlateItem plateItem) {
            ItemStackHandler newItemStackHandler = plateItem.copyItemStackHandlerByComponent(outputItem);
            spawnUsingConvertsTo(level, stackList, this.getBlockState(), this.getBlockPos());
            stackList.forEach(itemStack -> plateItem.insertItem(itemStack, newItemStackHandler));
            List<ItemStack> newStackList = getItemStackListInSlot(newItemStackHandler, 0, newItemStackHandler.getSlots());
            ItemStackHandler initialItemStackHandler = new ItemStackHandler(newItemStackHandler.getSlots());
            newStackList.forEach(itemStack -> plateItem.insertItem(itemStack.copy(), initialItemStackHandler));
            updateAll(outputItem, newItemStackHandler, initialItemStackHandler,
                    getFoodPropertyByPropertyValue(level, newStackList, this.getBlockState().getBlock(), true),
                    getConsumptionCount(newStackList), getConsumptionCount(newStackList),
                    lastInteractPlayerId, getProgress(level));
            spawnItemEntity(level, outputItem, this.getBlockState(), this.getBlockPos());
            lastInteractPlayerId = UUIDRecord.NULL.uuid();
        }
    }

    public void takeOutItem(Level level, BlockState state, BlockPos pos) {
        for (int i = 0; i < ingredientInput + seasoningInput + spiceInput + OUTPUT; i++) {
            ItemStack stackInSlot = getItemStackHandler().getStackInSlot(i);
            spawnItemEntity(level, stackInSlot, state, pos);
        }
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

    public void getRecipeCookingCompletionTime(RecipeHolder<? extends AbstractPotRecipe> holder){
        cookingCompletionTime = holder.value().getCookingTime();
    }

    public void getCustomizedCookingCompletionTime(){
        customizedCompletionTime = getTotalItemCount(getItemStackListInPot(false, true)) * 10;
    }

    public boolean isHeated() {
        if (level == null){
            return false;
        }
        BlockState stateBelow = level.getBlockState(worldPosition.below());
        if (!stateBelow.is(ModTag.HEAT_SOURCE)) {
            return false;
        }
        if (stateBelow.hasProperty(BlockStateProperties.LIT)){
            return stateBelow.getValue(BlockStateProperties.LIT);
        }
        if (stateBelow.hasProperty(LID)){
            return !stateBelow.getValue(LID).equals(PotState.WITHOUT_LID);
        }
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

    //speed up
    public void stirFryAccelerate(ItemStack itemStackInHand, Player player, InteractionHand hand, ItemStack spatula){
        if (!ItemStack.isSameItem(itemStackInHand, spatula)) {
            return;
        }
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
}
