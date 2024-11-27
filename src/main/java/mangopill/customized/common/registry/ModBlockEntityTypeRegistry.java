package mangopill.customized.common.registry;

import mangopill.customized.Customized;
import mangopill.customized.common.block.entity.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.*;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static mangopill.customized.common.util.RegistryUtil.*;

public class ModBlockEntityTypeRegistry {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPE = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Customized.MODID);
    //block
    public static final Supplier<BlockEntityType<BrushableBlockEntity>> SUSPICIOUS_DIRT = BLOCK_ENTITY_TYPE.register(
            "suspicious_dirt", basicBlockEntityType(BrushableBlockEntity::new, ModBlcokRegistry.SUSPICIOUS_DIRT));
    //kitchenware block
    public static final Supplier<BlockEntityType<CasseroleBlockEntity>> CASSEROLE = BLOCK_ENTITY_TYPE.register(
            "casserole", basicBlockEntityType(CasseroleBlockEntity::new, ModBlcokRegistry.CASSEROLE));
    public static final Supplier<BlockEntityType<SoupBowlBlockEntity>> SOUP_BOWL = BLOCK_ENTITY_TYPE.register(
            "soup_bowl", basicBlockEntityType(SoupBowlBlockEntity::new, ModBlcokRegistry.SOUP_BOWL));
}
