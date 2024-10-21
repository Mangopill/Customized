package mangopill.customized.common.registry;

import mangopill.customized.Customized;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.LootTable;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModLootTablesRegistry {
    public static final DeferredRegister<LootTable> LOOT_TABLE = DeferredRegister.create(Registries.LOOT_TABLE, Customized.MODID);
/*
    public static final ResourceKey<LootTable> SUSPICIOUS_DIRT = LOOT_TABLE.register()
    );*/
}
