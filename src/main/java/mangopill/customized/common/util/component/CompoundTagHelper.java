package mangopill.customized.common.util.component;

import mangopill.customized.common.FoodValue;
import mangopill.customized.common.block.handler.PotFluidHandler;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.*;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.fluids.FluidStack;

public final class CompoundTagHelper {
    private CompoundTagHelper() {}

    public static FoodProperties deserializeFoodPropertyNBT(CompoundTag compound) {
        CompoundTag foodPropertyTag = compound.getCompound("FoodProperty");
        return FoodProperties.DIRECT_CODEC.parse(NbtOps.INSTANCE, foodPropertyTag).result().orElse(FoodValue.EMPTY);
    }

    public static void putFoodPropertyTag(CompoundTag compound, FoodProperties foodProperty) {
        CompoundTag foodPropertyTag = (CompoundTag) FoodProperties.DIRECT_CODEC.encodeStart(NbtOps.INSTANCE, foodProperty).result().orElse(new CompoundTag());
        compound.put("FoodProperty", foodPropertyTag);
    }

    public static Ingredient deserializeIngredientNBT(CompoundTag compound) {
        CompoundTag containerTag = compound.getCompound("Container");
        return Ingredient.CODEC.parse(NbtOps.INSTANCE, containerTag).result().orElse(Ingredient.EMPTY);
    }

    public static void putIngredientTag(CompoundTag compound, Ingredient containerItem) {
        if (!containerItem.isEmpty()) {
            CompoundTag containerTag = (CompoundTag) Ingredient.CODEC.encodeStart(NbtOps.INSTANCE, containerItem).result().orElse(new CompoundTag());
            compound.put("Container", containerTag);
        }
    }

    public static void putFluidHandlerTag(CompoundTag compound, HolderLookup.Provider registries, PotFluidHandler potFluidHandler) {
        CompoundTag fluidHandlerTag = new CompoundTag();
        int tanks = potFluidHandler.getTanks();
        fluidHandlerTag.putInt("Tanks", tanks);
        for (int i = 0; i < tanks; i++) {
            fluidHandlerTag.put("Tank" + i, potFluidHandler.getFluidInTank(i).saveOptional(registries));
        }
        compound.put("FluidHandler", fluidHandlerTag);
    }

    public static void deserializeFluidHandlerTag(CompoundTag compound, HolderLookup.Provider registries, PotFluidHandler potFluidHandler) {
        CompoundTag fluidHandlerTag = compound.getCompound("FluidHandler");
        int tanks = fluidHandlerTag.getInt("Tanks");
        for (int i = 0; i < tanks; i++) {
            CompoundTag tankTag = fluidHandlerTag.getCompound("Tank" + i);
            FluidStack loadedFluid = FluidStack.parseOptional(registries, tankTag);
            potFluidHandler.setStoredFluidDirectly(loadedFluid);
        }
    }
}
