package mangopill.customized.common.registry;

import mangopill.customized.Customized;
import mangopill.customized.common.effect.combination.*;
import mangopill.customized.common.effect.normal.*;
import mangopill.customized.common.effect.powerful.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModEffectRegistry {
    public static final DeferredRegister<MobEffect> MOB_EFFECT = DeferredRegister.create(Registries.MOB_EFFECT, Customized.MODID);

    public static final RegistryObject<MobEffect> ICED = MOB_EFFECT.register("iced", () -> new IcedEffect(0x6CA3FD));
    public static final RegistryObject<MobEffect> WARM_STOMACH = MOB_EFFECT.register("warm_stomach", () -> new WarmStomachEffect(0xFFD700));

    public static final RegistryObject<MobEffect> VITALITY = MOB_EFFECT.register("vitality", () -> new VitalityEffect(0x00FA9A));
    public static final RegistryObject<MobEffect> ANTIDOTE = MOB_EFFECT.register("antidote", () -> new AntidoteEffect(0xFF8C00));
    public static final RegistryObject<MobEffect> SOAR = MOB_EFFECT.register("soar", () -> new SoarEffect(0x87CEEB));

    public static final RegistryObject<MobEffect> NOCTENERGY_SURGE = MOB_EFFECT.register("noctenergy_surge", () -> new NoctenergySurgeEffect(0x4B0A88));
    public static final RegistryObject<MobEffect> METABOLISM = MOB_EFFECT.register("metabolism", () -> new MetabolismEffect(0xFFCC00));
    public static final RegistryObject<MobEffect> ROBUST = MOB_EFFECT.register("robust", () -> new RobustEffect(0x3A5F7D));
    public static final RegistryObject<MobEffect> VITALITY_RESTORATION = MOB_EFFECT.register("vitality_restoration", () -> new VitalityRestorationEffect(0x4CAF50));
    public static final RegistryObject<MobEffect> CALORIE_BURN = MOB_EFFECT.register("calorie_burn", () -> new CalorieBurnEffect(0xFF4500));
    public static final RegistryObject<MobEffect> SMOOTH_BLOOD_FLOW = MOB_EFFECT.register("smooth_blood_flow", () -> new SmoothBloodFlowEffect(0x9B111E));
    public static final RegistryObject<MobEffect> MENTAL_STIMULATION = MOB_EFFECT.register("mental_stimulation", () -> new MentalStimulationEffect(0xFFD700));
    public static final RegistryObject<MobEffect> APPETITE_BOOST = MOB_EFFECT.register("appetite_boost", () -> new AppetiteBoostEffect(0xFF6347));
    public static final RegistryObject<MobEffect> SUSTAINED_ENERGY = MOB_EFFECT.register("sustained_energy", () -> new SustainedEnergyEffect(0xFFA500));
    public static final RegistryObject<MobEffect> STRESS_RELIEF = MOB_EFFECT.register("stress_relief", () -> new StressReliefEffect(0x87CEFA));
    public static final RegistryObject<MobEffect> HYDRATION_AND_PLUMPNESS = MOB_EFFECT.register("hydration_and_plumpness", () -> new HydrationAndPlumpnessEffect(0x00BFFF));
}
