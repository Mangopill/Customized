package mangopill.customized.common.registry;

import mangopill.customized.Customized;
import mangopill.customized.common.effect.*;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEffectRegistry {
    public static final DeferredRegister<MobEffect> MOB_EFFECT = DeferredRegister.create(Registries.MOB_EFFECT, Customized.MODID);

    public static final Holder<MobEffect> ICED = MOB_EFFECT.register("iced", () -> new IcedEffect(0x6CA3FD));
    public static final Holder<MobEffect> WARM_STOMACH = MOB_EFFECT.register("warm_stomach", () -> new WarmStomachEffect(0xFFD700));
}
