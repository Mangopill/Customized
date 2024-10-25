package mangopill.customized.common.registry;

import mangopill.customized.Customized;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModParticleTypeRegistry {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPE = DeferredRegister.create(Registries.PARTICLE_TYPE, Customized.MODID);

    public static final Supplier<SimpleParticleType> DIRT = PARTICLE_TYPE.register("dirt",
            () -> new SimpleParticleType(true));
}
