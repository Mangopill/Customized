package mangopill.customized.common.registry;

import com.mojang.serialization.Codec;
import mangopill.customized.Customized;
import mangopill.customized.common.util.record.*;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.neoforge.registries.*;

public final class CDataComponentRegistry {
    public static final DeferredRegister.DataComponents DATA_COMPONENT = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, Customized.MODID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ItemStackHandlerRecord>> ITEM_STACK_HANDLER = DATA_COMPONENT.registerComponentType(
            "item_stack_handler", builder -> builder.persistent(ItemStackHandlerRecord.CODEC).networkSynchronized(ItemStackHandlerRecord.STREAM_CODEC));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ItemStackHandlerRecord>> INITIAL_ITEM_STACK_HANDLER = DATA_COMPONENT.registerComponentType(
            "initial_item_stack_handler", builder -> builder.persistent(ItemStackHandlerRecord.CODEC).networkSynchronized(ItemStackHandlerRecord.STREAM_CODEC));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ConsumptionCountRecord>> CONSUMPTION_COUNT = DATA_COMPONENT.registerComponentType(
            "consumption_count", builder -> builder.persistent(ConsumptionCountRecord.CODEC).networkSynchronized(ConsumptionCountRecord.STREAM_CODEC));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ConsumptionCountTotalRecord>> CONSUMPTION_COUNT_TOTAL = DATA_COMPONENT.registerComponentType(
            "consumption_count_total", builder -> builder.persistent(ConsumptionCountTotalRecord.CODEC).networkSynchronized(ConsumptionCountTotalRecord.STREAM_CODEC));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<UUIDRecord>> UUID = DATA_COMPONENT.registerComponentType(
            "uuid", builder -> builder.persistent(UUIDRecord.CODEC).networkSynchronized(UUIDRecord.STREAM_CODEC));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> ADVANCEMENT_HAS_PROGRESS = DATA_COMPONENT.registerComponentType(
            "advancement_has_progress", builder -> builder.persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> ARGB_COLOR = DATA_COMPONENT.registerComponentType(
            "argb_color", builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.VAR_INT));
}
