package mangopill.customized.common.registry;

import mangopill.customized.Customized;
import mangopill.customized.common.block.entity.ModBrushableBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.*;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static mangopill.customized.common.StaticHelperMethod.basicBlockEntityType;

public class ModBlockEntityTypeRegistry {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Customized.MODID);


    public static final Supplier<BlockEntityType<ModBrushableBlockEntity>> SUSPICIOUS_DIRT = BLOCK_ENTITY_TYPES.register(
            "suspicious_dirt", basicBlockEntityType(ModBrushableBlockEntity::new, ModBlcokRegistry.SUSPICIOUS_DIRT)
    );
}
