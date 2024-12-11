package mangopill.customized.common.block.entity;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import mangopill.customized.common.FoodValue;
import mangopill.customized.common.block.AbstractPlateBlock;
import mangopill.customized.common.block.state.PlateState;
import mangopill.customized.common.registry.ModDataComponentRegistry;
import mangopill.customized.common.util.ModItemStackHandlerHelper;
import mangopill.customized.common.util.record.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.*;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.*;

import static mangopill.customized.common.util.ModItemStackHandlerHelper.*;
import static mangopill.customized.common.util.ModItemStackHandlerHelper.clearAllSlot;

public abstract class AbstractPlateBlockEntity extends BlockEntity {
    private final int ingredientInput;
    private final int seasoningInput;
    private final int SPICE_INPUT = 1;
    private final int allSlot;
    private final ItemStackHandler itemStackHandler;
    private FoodProperties foodProperty;
    private int consumptionCount;
    private int consumptionCountTotal;

    public AbstractPlateBlockEntity(BlockEntityType<? extends AbstractPlateBlockEntity> type, BlockPos pos, BlockState blockState, int ingredientInput, int seasoningInput) {
        super(type, pos, blockState);
        this.ingredientInput = ingredientInput;
        this.seasoningInput = seasoningInput;
        this.allSlot = ingredientInput + seasoningInput + SPICE_INPUT;
        this.itemStackHandler = createItemStackHandler();
    }

    protected ItemStackHandler createItemStackHandler() {
        return new ItemStackHandler(allSlot)
        {
            @Override
            protected void onContentsChanged(int slot) {
                itemStackHandlerChanged();
            }

            @Override
            protected void onLoad() {
                itemStackHandlerChanged();
            }
        };
    }

    public void itemStackHandlerChanged() {
        super.setChanged();
        if (level != null){
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
        }
    }

    public boolean hasInput() {
        return ModItemStackHandlerHelper.hasInput(itemStackHandler, allSlot);
    }

    //getItemStackList
    public List<ItemStack> getItemStackListInPlate(boolean includeSeasoningAndSpice) {
        return includeSeasoningAndSpice ? ModItemStackHandlerHelper.getItemStackListInSlot(itemStackHandler, 0, allSlot) :
                ModItemStackHandlerHelper.getItemStackListInSlot(itemStackHandler, 0, ingredientInput);
    }

    public void eatFood(@NotNull ItemStack stack, @NotNull Level level, @NotNull Player player, @NotNull BlockState state, @NotNull BlockPos pos) {
        if(consumptionCount >= 1) {
            level.playSound(null, player.getX(), player.getY(), player.getZ(), player.getEatingSound(stack),
                    SoundSource.NEUTRAL, 1.0F, 1.0F + (level.random.nextFloat() - level.random.nextFloat()) * 0.4F);
            addEffect(player, foodProperty);
            if (consumptionCount > 1){
                reduceItemStackCountByDivision(itemStackHandler, consumptionCountTotal);
            } else {
                clearAllSlot(itemStackHandler);
                clearFoodPropertyAndCountTotal();
                level.setBlockAndUpdate(pos, state.setValue(AbstractPlateBlock.DRIVE, PlateState.WITHOUT_DRIVE));
            }
            --consumptionCount;
            player.gameEvent(GameEvent.EAT);
            itemStackHandlerChanged();
        }
    }

    public static void addEffect(LivingEntity livingEntity, FoodProperties foodProperties) {
        if (!livingEntity.level().isClientSide()) {
            for (FoodProperties.PossibleEffect foodproperties$possibleeffect : foodProperties.effects()) {
                if (livingEntity.getRandom().nextFloat() < foodproperties$possibleeffect.probability()) {
                    livingEntity.addEffect(foodproperties$possibleeffect.effect());
                }
            }
            if (foodProperties.equals(FoodValue.INEDIBLE)) {
                livingEntity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 500, 1));
            }
        }
    }

    public void clearFoodPropertyAndCountTotal() {
       foodProperty = FoodValue.NULL;
       consumptionCount = 0;
    }

    public @NotNull ItemStack getCloneItemStack(ItemStack stack) {
        ItemStack itemStack = new ItemStack(stack.getItem());
        itemStack.applyComponents(collectComponents());
        return itemStack;
    }

    public CompoundTag @NotNull [] getFoodPropertyTagWrapper() {
        final CompoundTag[] foodPropertyTagWrapper = new CompoundTag[1];
        DataResult<Tag> encodeResult = FoodProperties.DIRECT_CODEC.encodeStart(NbtOps.INSTANCE, foodProperty);
        encodeResult.result().ifPresent(tag -> {
            if (tag instanceof CompoundTag) {
                foodPropertyTagWrapper[0] = (CompoundTag) tag;
            }
        });
        return foodPropertyTagWrapper;
    }

    @Override
    @Nullable
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void loadAdditional(@NotNull CompoundTag compound, HolderLookup.@NotNull Provider registries) {
        super.loadAdditional(compound, registries);
        consumptionCount = compound.getInt("ConsumptionCount");
        consumptionCountTotal = compound.getInt("ConsumptionCountTotal");
        itemStackHandler.deserializeNBT(registries, compound.getCompound("ItemStackHandler"));
        if (compound.contains("FoodProperty")) {
            CompoundTag foodPropertyTag = compound.getCompound("FoodProperty");
            DataResult<Pair<FoodProperties, Tag>> decodeResult = FoodProperties.DIRECT_CODEC.decode(NbtOps.INSTANCE, foodPropertyTag);
            decodeResult.result().ifPresent(pair -> {
                Tag tag = pair.getSecond();
                if (tag instanceof CompoundTag) {
                    foodProperty = FoodProperties.DIRECT_CODEC.parse(NbtOps.INSTANCE, tag).getOrThrow();
                }
            });
        } else {
            foodProperty = FoodValue.NULL;
        }
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag compound, HolderLookup.@NotNull Provider registries) {
        super.saveAdditional(compound, registries);
        compound.putInt("ConsumptionCount", consumptionCount);
        compound.putInt("ConsumptionCountTotal", consumptionCountTotal);
        compound.put("ItemStackHandler", itemStackHandler.serializeNBT(registries));
        final CompoundTag[] foodPropertyTagWrapper = getFoodPropertyTagWrapper();
        compound.put("FoodProperty", foodPropertyTagWrapper[0]);
    }



    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.@NotNull Provider registries) {
        CompoundTag tag = new CompoundTag();
        super.saveAdditional(tag, registries);
        tag.putInt("ConsumptionCount", consumptionCount);
        tag.putInt("ConsumptionCountTotal", consumptionCountTotal);
        tag.put("ItemStackHandler", itemStackHandler.serializeNBT(registries));
        final CompoundTag[] foodPropertyTagWrapper = getFoodPropertyTagWrapper();
        tag.put("FoodProperty", foodPropertyTagWrapper[0]);
        return tag;
    }

    @Override
    public void removeComponentsFromTag(CompoundTag tag) {
        tag.remove("ConsumptionCount");
        tag.remove("ConsumptionCountTotal");
        tag.remove("ItemStackHandler");
        tag.remove("FoodProperty");
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.@NotNull Builder component) {
        super.collectImplicitComponents(component);
        if (hasInput()) {
            component.set(ModDataComponentRegistry.CONSUMPTION_COUNT, new ConsumptionCountRecord(consumptionCount));
            component.set(ModDataComponentRegistry.CONSUMPTION_COUNT_TOTAL, new ConsumptionCountTotalRecord(consumptionCountTotal));
            component.set(DataComponents.FOOD, foodProperty);
            component.set(ModDataComponentRegistry.ITEM_STACK_HANDLER, new ItemStackHandlerRecord(itemStackHandler));
        }
    }

    @Override
    protected void applyImplicitComponents(BlockEntity.@NotNull DataComponentInput componentInput) {
        super.applyImplicitComponents(componentInput);
        consumptionCount = componentInput.getOrDefault(ModDataComponentRegistry.CONSUMPTION_COUNT, ConsumptionCountRecord.NULL).consumptionCount();
        consumptionCountTotal = componentInput.getOrDefault(ModDataComponentRegistry.CONSUMPTION_COUNT_TOTAL, ConsumptionCountTotalRecord.NULL).consumptionCountTotal();
        foodProperty = componentInput.getOrDefault(DataComponents.FOOD, FoodValue.NULL);
        ItemStackHandler componentItemStackHandler = componentInput.getOrDefault(ModDataComponentRegistry.ITEM_STACK_HANDLER, ItemStackHandlerRecord.NULL).itemStackHandler();
        List<ItemStack> stackList = getItemStackListInSlot(componentItemStackHandler, 0, allSlot);
        stackList.forEach(stack -> insertItem(stack.copy(), itemStackHandler, ingredientInput, seasoningInput, allSlot));
        itemStackHandlerChanged();
    }

    public int getIngredientInput() {
        return ingredientInput;
    }

    public int getSeasoningInput() {
        return seasoningInput;
    }

    public ItemStackHandler getItemStackHandler() {
        return itemStackHandler;
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
}
