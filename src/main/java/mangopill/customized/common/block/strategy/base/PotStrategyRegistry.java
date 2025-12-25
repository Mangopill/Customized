package mangopill.customized.common.block.strategy.base;

import mangopill.customized.Customized;
import mangopill.customized.common.block.record.PotRecord;
import mangopill.customized.common.block.strategy.pot.*;
import mangopill.customized.common.registry.*;
import mangopill.customized.common.tag.ModTag;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

@EventBusSubscriber(modid = Customized.MODID)
public class PotStrategyRegistry {
    public static final PotStrategyHandler HANDLER = PotStrategyHandler.getInstance();

    @SubscribeEvent
    public static void onPotRegistry(ServerStartingEvent event) {
        HANDLER.registry(CBlockRegistry.CASSEROLE.get().getDescriptionId(),
                new LidStrategy(PotRecord.CASSEROLE.lidItem().getDefaultInstance(), true),
                new FluidContainerStrategy(),
                new StirFryStrategy(CItemRegistry.SPOON.get().getDefaultInstance()),
                new InsertAndTakeOutItemStrategy());
        HANDLER.registry(CBlockRegistry.ROASTER.get().getDescriptionId(),
                new LidStrategy(PotRecord.ROASTER.lidItem().getDefaultInstance(), true),
                new IgniteStrategy(),
                new StirFryStrategy(CItemRegistry.SPATULA.get().getDefaultInstance()),
                new CancellableStrategy(ModTag.POT),
                new InsertAndTakeOutItemStrategy());
        HANDLER.registry(CBlockRegistry.WOK.get().getDescriptionId(),
                new LidStrategy(PotRecord.WOK.lidItem().getDefaultInstance(), true),
                new StirFryStrategy(CItemRegistry.SPATULA.get().getDefaultInstance()),
                new InsertAndTakeOutItemStrategy());
        HANDLER.registry(CBlockRegistry.STEAMER.get().getDescriptionId(),
                new LidStrategy(PotRecord.STEAMER.lidItem().getDefaultInstance(), true),
                new FluidContainerStrategy(),
                new StirFryStrategy(CItemRegistry.SPATULA.get().getDefaultInstance()),
                new InsertAndTakeOutItemStrategy());
    }
}