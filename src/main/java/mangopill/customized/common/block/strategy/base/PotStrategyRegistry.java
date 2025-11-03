package mangopill.customized.common.block.strategy.base;

import mangopill.customized.common.block.strategy.pot.*;
import mangopill.customized.common.registry.CBlockRegistry;
import mangopill.customized.common.registry.CItemRegistry;
import net.minecraft.world.item.ItemStack;

public class PotStrategyRegistry {
    public static final PotStrategyHandler HANDLER = PotStrategyHandler.getInstance();

    public static void onPotRegistry() {
        HANDLER.registry(CBlockRegistry.CASSEROLE.get().getDescriptionId(),
                new LidStrategy(new ItemStack(CItemRegistry.CASSEROLE_ILD.get()), true),
                new DriveStrategy(),
                new StirFryStrategy(),
                new InsertAndTakeOutItemStrategy());
    }
}
