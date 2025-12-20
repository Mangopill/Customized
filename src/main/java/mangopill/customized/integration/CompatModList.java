package mangopill.customized.integration;

import mangopill.customized.integration.curios.CuriosRegistry;

public enum CompatModList implements ICompatModList {
    JEI("jei", null),
    CURIOS("curios", new CuriosRegistry());

    private final String modId;
    private final ICompatModRegistry registry;

    CompatModList(String modId, ICompatModRegistry registry) {
        this.modId = modId;
        this.registry = registry;
    }

    @Override
    public String getModId() {
        return modId;
    }

    @Override
    public ICompatModRegistry getRegistry() {
        return registry;
    }
}
