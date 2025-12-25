package mangopill.customized.common.item.crafting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.*;
import net.minecraft.world.item.ItemStack;

import java.util.*;
import java.util.function.Consumer;

public record ProbabilityItemStack(float probability, NonNullList<ItemStack> probabilityStackList) {
    public static final ProbabilityItemStack EMPTY = new ProbabilityItemStack(0.0F, NonNullList.of(ItemStack.EMPTY));
    public static final Codec<ProbabilityItemStack> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.FLOAT.fieldOf("probability").forGetter(ProbabilityItemStack::probability),
                    NonNullList.codecOf(ItemStack.STRICT_CODEC).fieldOf("item").forGetter(ProbabilityItemStack::probabilityStackList)
            ).apply(instance, ProbabilityItemStack::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, ProbabilityItemStack> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.FLOAT, ProbabilityItemStack::probability, ByteBufCodecs.collection(NonNullList::createWithCapacity, ItemStack.STREAM_CODEC), ProbabilityItemStack::probabilityStackList, ProbabilityItemStack::new);

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(float probability) {
        return new Builder().probability(probability);
    }

    public static Builder builder(Consumer<Builder> consumer) {
        Builder builder = new Builder();
        consumer.accept(builder);
        return builder;
    }

    public Builder toBuilder() {
        return builder().probability(probability).addItems(probabilityStackList);
    }

    public boolean isEmpty() {
        return probabilityStackList.isEmpty() || equals(EMPTY);
    }

    public int size() {
        return probabilityStackList.size();
    }

    public static final class Builder {
        private float probability;
        private final NonNullList<ItemStack> stackList = NonNullList.create();

        private Builder() {
        }

        public Builder probability(float probability) {
            this.probability = probability;
            return this;
        }

        public Builder addItems(ItemStack... items) {
            return addItems(Arrays.asList(items));
        }

        public Builder addItems(Collection<ItemStack> items) {
            stackList.addAll(items.stream().map(ItemStack::copy).toList());
            return this;
        }

        public Builder clearItems() {
            stackList.clear();
            return this;
        }

        public Builder copy() {
            return new Builder().probability(probability).addItems(stackList);
        }

        public float getProbability() {
            return probability;
        }

        public boolean isEmpty() {
            return stackList.isEmpty();
        }

        public int size() {
            return stackList.size();
        }

        public ProbabilityItemStack build() {
            return new ProbabilityItemStack(probability, stackList);
        }

        public ProbabilityItemStack buildOrEmpty() {
            return stackList.isEmpty() ? EMPTY : build();
        }
    }
}
