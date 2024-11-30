package mangopill.customized.common.block.entity;

import mangopill.customized.common.FoodValue;
import mangopill.customized.common.registry.ModDataComponentRegistry;
import mangopill.customized.common.util.ModItemStackHandlerHelper;
import mangopill.customized.common.util.record.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

import static mangopill.customized.common.util.ModItemStackHandlerHelper.fillInItem;
import static mangopill.customized.common.util.ModItemStackHandlerHelper.getItemStackListInSlot;

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

    protected void itemStackHandlerChanged() {
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
                ModItemStackHandlerHelper.getItemStackListInSlot(itemStackHandler, 0, ingredientInput) ;
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
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag compound, HolderLookup.@NotNull Provider registries) {
        super.saveAdditional(compound, registries);
        compound.putInt("ConsumptionCount", consumptionCount);
        compound.putInt("ConsumptionCountTotal", consumptionCountTotal);
        compound.put("ItemStackHandler", itemStackHandler.serializeNBT(registries));
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.@NotNull Provider registries) {
        CompoundTag tag = new CompoundTag();
        super.saveAdditional(tag, registries);
        tag.put("ItemStackHandler", itemStackHandler.serializeNBT(registries));
        return tag;
    }

    @Override
    public void removeComponentsFromTag(CompoundTag tag) {
        tag.remove("ConsumptionCount");
        tag.remove("ConsumptionCountTotal");
        tag.remove("ItemStackHandler");
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.@NotNull Builder component) {
        super.collectImplicitComponents(component);
        if (hasInput() && !foodProperty.equals(FoodValue.NULL)) {
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
        stackList.forEach(stack -> fillInItem(itemStackHandler, stack.copy(), 0, allSlot));
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
}
