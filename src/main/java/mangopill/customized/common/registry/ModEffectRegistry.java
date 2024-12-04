package mangopill.customized.common.registry;

import mangopill.customized.Customized;
import mangopill.customized.common.effect.combination.*;
import mangopill.customized.common.effect.normal.*;
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

    public static final Holder<MobEffect> METABOLISM = MOB_EFFECT.register("metabolism", () -> new MetabolismEffect(0xFFCC00));
    public static final Holder<MobEffect> ROBUST = MOB_EFFECT.register("robust", () -> new RobustEffect(0x3A5F7D));
    public static final Holder<MobEffect> VITALITY_RESTORATION = MOB_EFFECT.register("vitality_restoration", () -> new VitalityRestorationEffect(0x4CAF50));
    public static final Holder<MobEffect> CALORIE_BURN = MOB_EFFECT.register("calorie_burn", () -> new CalorieBurnEffect(0xFF4500));
    public static final Holder<MobEffect> SMOOTH_BLOOD_FLOW = MOB_EFFECT.register("smooth_blood_flow", () -> new SmoothBloodFlowEffect(0x9B111E));
}
