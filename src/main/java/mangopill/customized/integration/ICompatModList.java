package mangopill.customized.integration;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.*;

import java.util.Collection;

public interface ICompatModList {
    String getModId();
    ICompatModRegistry getRegistry();

    static void registryAllCompat(Collection<ICompatModList> collection, IEventBus modBus, ModContainer container) {
        collection.stream().filter(list -> ModList.get().isLoaded(list.getModId()) && list.getRegistry() != null)
                .forEach(list -> list.getRegistry().registerCompat(modBus, container));
    }
}
