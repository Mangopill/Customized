package mangopill.customized.common.registry;

import mangopill.customized.Customized;
import mangopill.customized.common.block.entity.CasseroleBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.*;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static mangopill.customized.common.util.RegistryUtil.*;

public class ModBlockEntityTypeRegistry {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Customized.MODID);


    public static final Supplier<BlockEntityType<BrushableBlockEntity>> SUSPICIOUS_DIRT = BLOCK_ENTITY_TYPES.register(
            "suspicious_dirt", basicBlockEntityType(BrushableBlockEntity::new, ModBlcokRegistry.SUSPICIOUS_DIRT));

    public static final Supplier<BlockEntityType<CasseroleBlockEntity>> CASSEROLE = BLOCK_ENTITY_TYPES.register("casserole",
            () -> BlockEntityType.Builder.of(CasseroleBlockEntity::new, ModBlcokRegistry.CASSEROLE.get()).build(null));
}
