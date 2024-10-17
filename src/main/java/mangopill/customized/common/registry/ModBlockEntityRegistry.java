package mangopill.customized.common.registry;

import mangopill.customized.Customized;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BrushableBlockEntity;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlockEntityRegistry {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Customized.MODID);

    public static final Supplier<BlockEntityType<BrushableBlockEntity>> SUSPICIOUS_DIRT = BLOCK_ENTITY_TYPES.register(
            "suspicious_dirt",
            () -> BlockEntityType.Builder.of(
                            BrushableBlockEntity::new,
                            ModBlcokRegistry.SUSPICIOUS_DIRT.get()
                    )
                    .build(null)
    );
}
