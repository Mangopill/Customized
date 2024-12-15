package mangopill.customized.common.registry;

import mangopill.customized.Customized;
import mangopill.customized.common.advancement.BasicTrigger;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModAdvancementRegistry {
    public static final DeferredRegister<CriterionTrigger<?>> TRIGGER = DeferredRegister.create(Registries.TRIGGER_TYPE, Customized.MODID);

    public static final Supplier<BasicTrigger> WASH_SEEDS =
            TRIGGER.register("wash_seeds", BasicTrigger::new);
    public static final Supplier<BasicTrigger> USE_BREWING_BARREL =
            TRIGGER.register("use_brewing_barrel", BasicTrigger::new);
    public static final Supplier<BasicTrigger> EAT_NORMAL_STEW =
            TRIGGER.register("eat_normal_stew", BasicTrigger::new);
    public static final Supplier<BasicTrigger> EAT_INEDIBLE_STEW =
            TRIGGER.register("eat_inedible_stew", BasicTrigger::new);
    public static final Supplier<BasicTrigger> GET_FAMOUS_DISH =
            TRIGGER.register("get_famous_dish", BasicTrigger::new);
}
