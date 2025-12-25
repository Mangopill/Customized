package mangopill.customized.common.registry;

import mangopill.customized.Customized;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class CFluidRegistry {
    public static final DeferredRegister<Fluid> FLUID = DeferredRegister.create(Registries.FLUID, Customized.MODID);

    public static final Supplier<BaseFlowingFluid> SOUP = FLUID.register(
            "soup", () -> new BaseFlowingFluid.Source(CFluidRegistry.SOUP_FLUID_PROPERTIES));
    public static final Supplier<BaseFlowingFluid> FLOWING_SOUP = FLUID.register(
            "flowing_soup", () -> new BaseFlowingFluid.Flowing(CFluidRegistry.SOUP_FLUID_PROPERTIES));
    private static final BaseFlowingFluid.Properties SOUP_FLUID_PROPERTIES = new BaseFlowingFluid.Properties(CFluidTypeRegistry.SOUP_TYPE, CFluidRegistry.SOUP, CFluidRegistry.FLOWING_SOUP).bucket(CItemRegistry.SOUP_BUCKET).block(CBlockRegistry.SOUP);
}