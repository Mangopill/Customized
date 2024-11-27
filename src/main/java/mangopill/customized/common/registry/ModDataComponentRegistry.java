package mangopill.customized.common.registry;

import mangopill.customized.Customized;
import mangopill.customized.common.util.record.ConsumptionCountRecord;
import mangopill.customized.common.util.record.ConsumptionCountTotalRecord;
import mangopill.customized.common.util.record.ItemStackHandlerRecord;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModDataComponentRegistry {
    public static final DeferredRegister.DataComponents DATA_COMPONENT = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, Customized.MODID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ItemStackHandlerRecord>> ITEM_STACK_HANDLER = DATA_COMPONENT.registerComponentType(
            "item_stack_handler", builder -> builder.persistent(ItemStackHandlerRecord.CODEC).networkSynchronized(ItemStackHandlerRecord.STREAM_CODEC));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ConsumptionCountRecord>> CONSUMPTION_COUNT = DATA_COMPONENT.registerComponentType(
            "consumption_count", builder -> builder.persistent(ConsumptionCountRecord.CODEC).networkSynchronized(ConsumptionCountRecord.STREAM_CODEC));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ConsumptionCountTotalRecord>> CONSUMPTION_COUNT_TOTAL = DATA_COMPONENT.registerComponentType(
            "consumption_count_total", builder -> builder.persistent(ConsumptionCountTotalRecord.CODEC).networkSynchronized(ConsumptionCountTotalRecord.STREAM_CODEC));
}
