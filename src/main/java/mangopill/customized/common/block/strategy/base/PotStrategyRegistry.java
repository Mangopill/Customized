package mangopill.customized.common.block.strategy.base;

import mangopill.customized.common.block.strategy.pot.*;
import mangopill.customized.common.registry.ModBlockRegistry;
import mangopill.customized.common.registry.ModItemRegistry;
import net.minecraft.world.item.ItemStack;

public class PotStrategyRegistry {
    public static final PotStrategyHandler HANDLER = PotStrategyHandler.getInstance();

    public static void onPotRegistry() {
        HANDLER.registry(ModBlockRegistry.CASSEROLE.get().getDescriptionId(),
                new LidStrategy(new ItemStack(ModItemRegistry.CASSEROLE_ILD.get()), true),
                new DishStrategy(),
                new DriveStrategy(),
                new InsertAndTakeOutItemStrategy());
    }
}
