package mangopill.customized.common.registry;

import mangopill.customized.Customized;
import mangopill.customized.common.advancement.CBasicTrigger;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class CAdvancementRegistry {
    public static final DeferredRegister<CriterionTrigger<?>> TRIGGER = DeferredRegister.create(Registries.TRIGGER_TYPE, Customized.MODID);

    public static final Supplier<CBasicTrigger> WASH_SEEDS = TRIGGER.register("wash_seeds", CBasicTrigger::new);
    public static final Supplier<CBasicTrigger> USE_BREWING_BARREL = TRIGGER.register("use_brewing_barrel", CBasicTrigger::new);
    public static final Supplier<CBasicTrigger> EAT_NORMAL_STEW = TRIGGER.register("eat_normal_stew", CBasicTrigger::new);
    public static final Supplier<CBasicTrigger> EAT_INEDIBLE_STEW = TRIGGER.register("eat_inedible_stew", CBasicTrigger::new);
    public static final Supplier<CBasicTrigger> GET_FAMOUS_DISH = TRIGGER.register("get_famous_dish", CBasicTrigger::new);
    public static final Supplier<CBasicTrigger> USE_FLYING_KNIFE = TRIGGER.register("use_flying_knife", CBasicTrigger::new);
}
