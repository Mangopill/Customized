package mangopill.customized.common.registry;

import mangopill.customized.Customized;
import mangopill.customized.common.effect.normal.IcedEffect;
import mangopill.customized.common.effect.powerful.VitalityEffect;
import mangopill.customized.common.effect.normal.WarmStomachEffect;
import mangopill.customized.common.effect.powerful.*;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEffectRegistry {
    public static final DeferredRegister<MobEffect> MOB_EFFECT = DeferredRegister.create(Registries.MOB_EFFECT, Customized.MODID);

    public static final Holder<MobEffect> ICED = MOB_EFFECT.register("iced", () -> new IcedEffect(0x6CA3FD));
    public static final Holder<MobEffect> WARM_STOMACH = MOB_EFFECT.register("warm_stomach", () -> new WarmStomachEffect(0xFFD700));

    public static final Holder<MobEffect> VITALITY = MOB_EFFECT.register("vitality", () -> new VitalityEffect(0x00FA9A));
    public static final Holder<MobEffect> ANTIDOTE = MOB_EFFECT.register("antidote", () -> new AntidoteEffect(0xFF8C00));
    public static final Holder<MobEffect> SOAR = MOB_EFFECT.register("soar", () -> new SoarEffect(0x87CEEB));
}
