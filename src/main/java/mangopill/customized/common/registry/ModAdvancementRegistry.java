package mangopill.customized.common.registry;

import mangopill.customized.Customized;
import mangopill.customized.common.advancement.BasicTrigger;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.resources.ResourceLocation;

public class ModAdvancementRegistry {
    public static final BasicTrigger WASH_SEEDS = new BasicTrigger(getId("wash_seeds"));
    public static final BasicTrigger USE_BREWING_BARREL = new BasicTrigger(getId("use_brewing_barrel"));
    public static final BasicTrigger EAT_NORMAL_STEW = new BasicTrigger(getId("eat_normal_stew"));
    public static final BasicTrigger EAT_INEDIBLE_STEW = new BasicTrigger(getId("eat_inedible_stew"));
    public static final BasicTrigger GET_FAMOUS_DISH = new BasicTrigger(getId("get_famous_dish"));

    public static ResourceLocation getId(String name) {
        return new ResourceLocation(Customized.MODID, name);
    }

    public static void register() {
        CriteriaTriggers.register(WASH_SEEDS);
        CriteriaTriggers.register(USE_BREWING_BARREL);
        CriteriaTriggers.register(EAT_NORMAL_STEW);
        CriteriaTriggers.register(EAT_INEDIBLE_STEW);
        CriteriaTriggers.register(GET_FAMOUS_DISH);
    }
}
