package mangopill.customized.common.util.record;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;

import java.util.ArrayList;
import java.util.List;

public record ItemStackHandlerRecord(ItemStackHandler itemStackHandler) {
    public static final ItemStackHandlerRecord NULL = new ItemStackHandlerRecord(new ItemStackHandler());
    public static final Codec<ItemStackHandler> ITEM_STACK_HANDLER_CODEC = RecordCodecBuilder.create(instance ->
            instance.group(Codec.INT.fieldOf("Slots").forGetter(ItemStackHandler::getSlots),
                    Codec.list(ItemStack.OPTIONAL_CODEC).fieldOf("Items").forGetter(handler -> {
                        List<ItemStack> stacks = new ArrayList<>();
                        for (int i = 0; i < handler.getSlots(); i++) {
                            stacks.add(handler.getStackInSlot(i).copy());
                        }
                        return stacks;
                    })).apply(instance, (slots, items) -> {
                ItemStackHandler handler = new ItemStackHandler(slots);
                for (int i = 0; i < items.size(); i++) {
                    handler.setStackInSlot(i, items.get(i).copy());
                }
                return handler;
            }));
    public static final StreamCodec<RegistryFriendlyByteBuf, ItemStackHandler> ITEM_STACK_HANDLER_STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            ItemStackHandler::getSlots,
            ItemStack.OPTIONAL_STREAM_CODEC.apply(ByteBufCodecs.list()),
            handler -> {
                List<ItemStack> stacks = new ArrayList<>();
                for (int i = 0; i < handler.getSlots(); i++) {
                    stacks.add(handler.getStackInSlot(i).copy());
                }
                return stacks;
            },
            (slots, items) -> {
                ItemStackHandler handler = new ItemStackHandler(slots);
                for (int i = 0; i < items.size(); i++) {
                    handler.setStackInSlot(i, items.get(i).copy());
                }
                return handler;
            }
    );
    public static final Codec<ItemStackHandlerRecord> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ITEM_STACK_HANDLER_CODEC.fieldOf("item_stack_handler").forGetter(ItemStackHandlerRecord::itemStackHandler)
            ).apply(instance, ItemStackHandlerRecord::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, ItemStackHandlerRecord> STREAM_CODEC = StreamCodec.composite(
            ITEM_STACK_HANDLER_STREAM_CODEC, ItemStackHandlerRecord::itemStackHandler, ItemStackHandlerRecord::new
    );
}
