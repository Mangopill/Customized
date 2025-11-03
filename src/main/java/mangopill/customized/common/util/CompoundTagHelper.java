package mangopill.customized.common.util;

import mangopill.customized.common.FoodValue;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.crafting.Ingredient;

public final class CompoundTagHelper {
    private CompoundTagHelper() {
    }

    public static FoodProperties deserializeFoodPropertyNBT(CompoundTag compound) {
        CompoundTag foodPropertyTag = compound.getCompound("FoodProperty");
        return FoodProperties.DIRECT_CODEC.parse(NbtOps.INSTANCE, foodPropertyTag)
                .result().orElse(FoodValue.NULL);
    }

    public static void putFoodPropertyTag(CompoundTag compound, FoodProperties foodProperty) {
        CompoundTag foodPropertyTag = (CompoundTag) FoodProperties.DIRECT_CODEC.encodeStart(NbtOps.INSTANCE, foodProperty)
                .result().orElse(new CompoundTag());
        compound.put("FoodProperty", foodPropertyTag);
    }

    public static Ingredient deserializeIngredientNBT(CompoundTag compound) {
        CompoundTag containerTag = compound.getCompound("Container");
        return Ingredient.CODEC.parse(NbtOps.INSTANCE, containerTag)
                .result().orElse(Ingredient.EMPTY);
    }

    public static void putIngredientTag(CompoundTag compound, Ingredient containerItem) {
        if (!containerItem.isEmpty()) {
            CompoundTag containerTag = (CompoundTag) Ingredient.CODEC.encodeStart(NbtOps.INSTANCE, containerItem)
                    .result().orElse(new CompoundTag());
            compound.put("Container", containerTag);
        }
    }
}
