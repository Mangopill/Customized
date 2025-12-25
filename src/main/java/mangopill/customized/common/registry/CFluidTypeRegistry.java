package mangopill.customized.common.registry;

import mangopill.customized.Customized;
import mangopill.customized.common.fluid.type.SoupFluidType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.pathfinder.PathType;
import net.neoforged.neoforge.common.SoundActions;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.*;

import java.util.function.Supplier;

import static mangopill.customized.common.util.CStringUtil.*;

public final class CFluidTypeRegistry {
    public static final DeferredRegister<FluidType> FLUID_TYPE = DeferredRegister.create(NeoForgeRegistries.Keys.FLUID_TYPES, Customized.MODID);


    private static final ResourceLocation SOUP_STILL = getCLoc("block/soup_still");
    private static final ResourceLocation SOUP_FLOW = getCLoc("block/soup_flow");
    public static final Supplier<FluidType> SOUP_TYPE = FLUID_TYPE.register(
            "soup", () -> new SoupFluidType(FluidType.Properties.create()
                    .density(1200).viscosity(2500).temperature(350).motionScale(0.008)
                    .fallDistanceModifier(0.3F).canSwim(false).canDrown(true).canPushEntity(false)
                    .canExtinguish(true).canConvertToSource(false).supportsBoating(false).canHydrate(false)
                    .lightLevel(0).rarity(Rarity.UNCOMMON).pathType(PathType.WATER).adjacentPathType(PathType.WATER_BORDER)
                    .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL).sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)
                    .sound(SoundActions.FLUID_VAPORIZE, SoundEvents.FIRE_EXTINGUISH)
                    .addDripstoneDripping(0.1F, ParticleTypes.DRIPPING_WATER, Blocks.CAULDRON, SoundEvents.BUCKET_FILL),
                    SOUP_STILL, SOUP_FLOW));
}
