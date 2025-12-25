package mangopill.customized.common.block.entity;

import mangopill.customized.common.FoodValue;
import mangopill.customized.common.block.AbstractPlateBlock;
import mangopill.customized.common.block.record.PlateRecord;
import mangopill.customized.common.block.state.PlateState;
import mangopill.customized.common.item.AbstractPlateItem;
import mangopill.customized.common.registry.CDataComponentRegistry;
import mangopill.customized.common.util.*;
import mangopill.customized.common.util.record.*;
import net.minecraft.core.*;
import net.minecraft.core.component.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.registries.DeferredHolder;

import javax.annotation.Nullable;
import java.util.*;

import static mangopill.customized.common.util.CItemStackHandlerHelper.*;
import static mangopill.customized.common.util.CompoundTagHelper.*;

public abstract class AbstractPlateBlockEntity extends BlockEntity implements CreateItemStackHandler {
    private final int ingredientInput;
    private final int seasoningInput;
    private final int spiceInput;
    private final int allSlot;
    private final ItemStackHandler itemStackHandler;
    private final ItemStackHandler initialItemStackHandler;
    private FoodProperties foodProperty;
    private int consumptionCount;
    private int consumptionCountTotal;
    private UUID lastInteractPlayerId;
    private Boolean advancementHasProgress;

    protected AbstractPlateBlockEntity(BlockEntityType<? extends AbstractPlateBlockEntity> type, BlockPos pos, BlockState blockState, int ingredientInput, int seasoningInput, int spiceInput) {
        super(type, pos, blockState);
        this.ingredientInput = ingredientInput;
        this.seasoningInput = seasoningInput;
        this.spiceInput = spiceInput;
        this.allSlot = ingredientInput + seasoningInput + spiceInput;
        this.itemStackHandler = createItemStackHandler(allSlot);
        this.initialItemStackHandler = createItemStackHandler(allSlot);
        this.lastInteractPlayerId = UUIDRecord.EMPTY.uuid();
    }

    protected <T extends AbstractPlateBlockEntity> AbstractPlateBlockEntity(BlockPos pos, BlockState blockState, PlateRecord<T> record) {
        this(record.type().get(), pos, blockState, record.ingredientInput(), record.seasoningInput(), record.spiceInput());
    }

    @Override
    public void itemStackHandlerChanged() {
        super.setChanged();
        if (level != null){
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
        }
    }

    public boolean hasInput() {
        return CItemStackHandlerHelper.hasInput(itemStackHandler, allSlot);
    }

    public List<ItemStack> getItemStackListInPlate(boolean includeSeasoningAndSpice) {
        return includeSeasoningAndSpice ? CItemStackHandlerHelper.getItemStackListInSlot(itemStackHandler, 0, allSlot) :
                CItemStackHandlerHelper.getItemStackListInSlot(itemStackHandler, 0, ingredientInput);
    }

    public void eatFood(Level level, Player player, BlockState state, BlockPos pos) {
        if (consumptionCount < 1) return;
        level.playSound(null, pos, SoundEvents.GENERIC_EAT, SoundSource.PLAYERS, 0.8F, 0.8F);
        player.getFoodData().eat(foodProperty);
        addEffect(player, foodProperty);
        AbstractPlateItem.plateAdvancement(player, foodProperty);
        if (consumptionCount > 1){
            reduceItemStackCountByDivision(itemStackHandler, initialItemStackHandler, consumptionCountTotal);
        } else {
            clearAllSlot(itemStackHandler);
            clearAllSlot(initialItemStackHandler);
            clearFoodPropertyAndCountTotal();
            level.setBlockAndUpdate(pos, state.setValue(AbstractPlateBlock.DRIVE, PlateState.WITHOUT_DRIVE));
        }
        --consumptionCount;
        player.gameEvent(GameEvent.EAT);
        itemStackHandlerChanged();
    }

    public static void addEffect(LivingEntity livingEntity, FoodProperties foodProperties) {
        if (livingEntity.level().isClientSide()) return;
        for (FoodProperties.PossibleEffect foodproperties$possibleeffect : foodProperties.effects()) {
            if (livingEntity.getRandom().nextFloat() < foodproperties$possibleeffect.probability()) {
                livingEntity.addEffect(foodproperties$possibleeffect.effect());
            }
        }
        if (foodProperties.equals(FoodValue.INEDIBLE)) {
            livingEntity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 500, 1));
        }
    }

    public void clearFoodPropertyAndCountTotal() {
       foodProperty = FoodValue.EMPTY;
       consumptionCountTotal = 0;
    }

    public ItemStack getCloneItemStack(ItemStack stack) {
        ItemStack itemStack = new ItemStack(stack.getItem());
        itemStack.applyComponents(collectComponents());
        return itemStack;
    }

    @Override
    @Nullable
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void loadAdditional(CompoundTag compound, HolderLookup.Provider registries) {
        super.loadAdditional(compound, registries);
        consumptionCount = compound.getInt("ConsumptionCount");
        consumptionCountTotal = compound.getInt("ConsumptionCountTotal");
        itemStackHandler.deserializeNBT(registries, compound.getCompound("ItemStackHandler"));
        initialItemStackHandler.deserializeNBT(registries, compound.getCompound("InitialItemStackHandler"));
        if (compound.contains("FoodProperty")) {
            foodProperty = deserializeFoodPropertyNBT(compound);
        }
        lastInteractPlayerId = compound.getUUID("LastInteractPlayerId");
        advancementHasProgress = compound.getBoolean("AdvancementHasProgress");
    }

    @Override
    public void saveAdditional(CompoundTag compound, HolderLookup.Provider registries) {
        super.saveAdditional(compound, registries);
        compound.putInt("ConsumptionCount", consumptionCount);
        compound.putInt("ConsumptionCountTotal", consumptionCountTotal);
        compound.put("ItemStackHandler", itemStackHandler.serializeNBT(registries));
        compound.put("InitialItemStackHandler", initialItemStackHandler.serializeNBT(registries));
        putFoodPropertyTag(compound, foodProperty);
        compound.putUUID("LastInteractPlayerId", lastInteractPlayerId);
        compound.putBoolean("AdvancementHasProgress", advancementHasProgress);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag, registries);
        return tag;
    }

    @Override
    public void removeComponentsFromTag(CompoundTag tag) {
        tag.remove("ConsumptionCount");
        tag.remove("ConsumptionCountTotal");
        tag.remove("ItemStackHandler");
        tag.remove("InitialItemStackHandler");
        tag.remove("FoodProperty");
        tag.remove("LastInteractPlayerId");
        tag.remove("AdvancementHasProgress");
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder component) {
        super.collectImplicitComponents(component);
        if (!hasInput()) return;
        component.set(CDataComponentRegistry.CONSUMPTION_COUNT, new ConsumptionCountRecord(consumptionCount));
        component.set(CDataComponentRegistry.CONSUMPTION_COUNT_TOTAL, new ConsumptionCountTotalRecord(consumptionCountTotal));
        component.set(DataComponents.FOOD, foodProperty);
        component.set(CDataComponentRegistry.ITEM_STACK_HANDLER, new ItemStackHandlerRecord(itemStackHandler));
        component.set(CDataComponentRegistry.INITIAL_ITEM_STACK_HANDLER, new ItemStackHandlerRecord(initialItemStackHandler));
        component.set(CDataComponentRegistry.UUID, new UUIDRecord(lastInteractPlayerId));
        component.set(CDataComponentRegistry.ADVANCEMENT_HAS_PROGRESS, advancementHasProgress);
    }

    @Override
    protected void applyImplicitComponents(BlockEntity.DataComponentInput componentInput) {
        super.applyImplicitComponents(componentInput);
        consumptionCount = componentInput.getOrDefault(CDataComponentRegistry.CONSUMPTION_COUNT, ConsumptionCountRecord.NULL).consumptionCount();
        consumptionCountTotal = componentInput.getOrDefault(CDataComponentRegistry.CONSUMPTION_COUNT_TOTAL, ConsumptionCountTotalRecord.NULL).consumptionCountTotal();
        foodProperty = componentInput.getOrDefault(DataComponents.FOOD, FoodValue.EMPTY);
        processComponentStack(componentInput, CDataComponentRegistry.ITEM_STACK_HANDLER, itemStackHandler);
        processComponentStack(componentInput, CDataComponentRegistry.INITIAL_ITEM_STACK_HANDLER, initialItemStackHandler);
        lastInteractPlayerId = componentInput.getOrDefault(CDataComponentRegistry.UUID, UUIDRecord.EMPTY).uuid();
        advancementHasProgress = componentInput.getOrDefault(CDataComponentRegistry.ADVANCEMENT_HAS_PROGRESS, false);
    }

    protected void processComponentStack(BlockEntity.DataComponentInput componentInput,
                                       DeferredHolder<DataComponentType<?>, DataComponentType<ItemStackHandlerRecord>> componentType,
                                       ItemStackHandler itemStackHandler) {
        ItemStackHandler componentHandler = componentInput
                .getOrDefault(componentType, ItemStackHandlerRecord.EMPTY)
                .itemStackHandler();
        List<ItemStack> stacks = getItemStackListInSlot(componentHandler, 0, componentHandler.getSlots());
        stacks.forEach(stack ->
                insertItem(stack.copy(), itemStackHandler, ingredientInput, seasoningInput, spiceInput, 0, null)
        );
    }

    public int getIngredientInput() {
        return ingredientInput;
    }

    public int getSeasoningInput() {
        return seasoningInput;
    }

    public int geSpiceInput() {
        return spiceInput;
    }

    public ItemStackHandler getItemStackHandler() {
        return itemStackHandler;
    }

    public ItemStackHandler getInitialItemStackHandler() {
        return initialItemStackHandler;
    }

    public FoodProperties getFoodProperty() {
        return foodProperty;
    }

    public int getConsumptionCountTotal() {
        return consumptionCountTotal;
    }

    public int getConsumptionCount() {
        return consumptionCount;
    }

    public UUID getLastInteractPlayerId() {
        return lastInteractPlayerId;
    }

    public Boolean getAdvancementHasProgress() {
        return advancementHasProgress;
    }
}
