package mangopill.customized.integration;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;

@FunctionalInterface
public interface ICompatModRegistry {
    void registerCompat(IEventBus modBus, ModContainer container);
}
