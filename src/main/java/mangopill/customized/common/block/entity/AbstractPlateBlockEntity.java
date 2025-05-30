package mangopill.customized.common.block.entity;

import com.mojang.datafixers.util.Pair;
import mangopill.customized.common.FoodValue;
import mangopill.customized.common.block.AbstractPlateBlock;
import mangopill.customized.common.block.state.PlateState;
import mangopill.customized.common.item.AbstractPlateItem;
import mangopill.customized.common.util.*;
import net.minecraft.core.*;
import net.minecraft.nbt.*;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.*;
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
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

import static mangopill.customized.common.util.ModItemStackHandlerHelper.*;
import static mangopill.customized.common.util.ModItemStackHandlerHelper.clearAllSlot;

public abstract class AbstractPlateBlockEntity extends BlockEntity implements CreateItemStackHandler {
    private final int ingredientInput;
    private final int seasoningInput;
    private static final int SPICE_INPUT = 1;
    private final int allSlot;
    private final ItemStackHandler itemStackHandler;
    private final ItemStackHandler initialItemStackHandler;
    private FoodProperties foodProperty;
    private int consumptionCount;
    private int consumptionCountTotal;

    public AbstractPlateBlockEntity(BlockEntityType<? extends AbstractPlateBlockEntity> type, BlockPos pos, BlockState blockState, int ingredientInput, int seasoningInput) {
        super(type, pos, blockState);
        this.ingredientInput = ingredientInput;
        this.seasoningInput = seasoningInput;
        this.foodProperty = FoodValue.NULL;
        this.allSlot = ingredientInput + seasoningInput + SPICE_INPUT;
        this.itemStackHandler = createItemStackHandler(allSlot);
        this.initialItemStackHandler = createItemStackHandler(allSlot);
    }

    @Override
    public void itemStackHandlerChanged() {
        super.setChanged();
        if (level != null){
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
        }
    }

    public boolean hasInput() {
        return ModItemStackHandlerHelper.hasInput(itemStackHandler, allSlot);
    }

    public List<ItemStack> getItemStackListInPlate(boolean includeSeasoningAndSpice) {
        return includeSeasoningAndSpice ? ModItemStackHandlerHelper.getItemStackListInSlot(itemStackHandler, 0, allSlot) :
                ModItemStackHandlerHelper.getItemStackListInSlot(itemStackHandler, 0, ingredientInput);
    }

    public void eatFood(@Nonnull Level level, @Nonnull Player player, @Nonnull BlockState state, @Nonnull BlockPos pos) {
        if(consumptionCount >= 1) {
            level.playSound(null, pos, SoundEvents.GENERIC_EAT, SoundSource.PLAYERS, 0.8F, 0.8F);
            player.getFoodData().eat(foodProperty.getNutrition(), foodProperty.getSaturationModifier());
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
    }

    public static void addEffect(LivingEntity livingEntity, FoodProperties foodProperties) {
        if (!livingEntity.level().isClientSide()) {
            for (Pair<MobEffectInstance, Float> possibleeffect : foodProperties.getEffects()) {
                if (livingEntity.getRandom().nextFloat() < possibleeffect.getSecond()) {
                    livingEntity.addEffect(possibleeffect.getFirst());
                }
            }
            if (!foodProperties.canAlwaysEat()) {
                livingEntity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 500, 1));
            }
        }
    }

    public void clearFoodPropertyAndCountTotal() {
       foodProperty = FoodValue.NULL;
       consumptionCountTotal = 0;
    }

    public @Nonnull ItemStack getCloneItemStack(ItemStack stack) {
        ItemStack itemStack = new ItemStack(stack.getItem(), stack.getCount());
        this.setChanged();
        itemStack.setTag(getUpdateTag());
        return itemStack;
    }

    public void setFoodProperty(@Nullable FoodProperties foodProperty) {
        this.foodProperty = foodProperty != null ? foodProperty : FoodValue.NULL;
    }

    @Override
    @Nullable
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void load(@Nonnull CompoundTag compound) {
        super.load(compound);
        consumptionCount = compound.getInt("ConsumptionCount");
        consumptionCountTotal = compound.getInt("ConsumptionCountTotal");
        itemStackHandler.deserializeNBT(compound.getCompound("ItemStackHandler"));
        initialItemStackHandler.deserializeNBT(compound.getCompound("InitialItemStackHandler"));
        setFoodProperty(PlateComponentUtil.deserializeFoodProperties(compound.getCompound("FoodProperty")));
    }

    @Override
    public void saveAdditional(@Nonnull CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putInt("ConsumptionCount", consumptionCount);
        compound.putInt("ConsumptionCountTotal", consumptionCountTotal);
        compound.put("ItemStackHandler", itemStackHandler.serializeNBT());
        compound.put("InitialItemStackHandler", initialItemStackHandler.serializeNBT());
        compound.put("FoodProperty", PlateComponentUtil.serializeFoodProperties(foodProperty));
    }

    @Override
    public @Nonnull CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        super.saveAdditional(tag);
        tag.putInt("ConsumptionCount", consumptionCount);
        tag.putInt("ConsumptionCountTotal", consumptionCountTotal);
        tag.put("ItemStackHandler", itemStackHandler.serializeNBT());
        tag.put("InitialItemStackHandler", initialItemStackHandler.serializeNBT());
        tag.put("FoodProperty", PlateComponentUtil.serializeFoodProperties(foodProperty));
        return tag;
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
}
