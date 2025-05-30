package mangopill.customized.common.registry;

import mangopill.customized.Customized;
import mangopill.customized.common.block.entity.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.*;
import net.minecraftforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static mangopill.customized.common.util.RegistryUtil.*;

public class ModBlockEntityTypeRegistry {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPE = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Customized.MODID);
    //block
    public static final Supplier<BlockEntityType<ModBrushableBlockEntity>> SUSPICIOUS_DIRT = BLOCK_ENTITY_TYPE.register(
            "suspicious_dirt", basicBlockEntityType(ModBrushableBlockEntity::new, ModBlockRegistry.SUSPICIOUS_DIRT));
    //kitchenware block
    public static final Supplier<BlockEntityType<CasseroleBlockEntity>> CASSEROLE = BLOCK_ENTITY_TYPE.register(
            "casserole", basicBlockEntityType(CasseroleBlockEntity::new, ModBlockRegistry.CASSEROLE));
    public static final Supplier<BlockEntityType<SoupBowlBlockEntity>> SOUP_BOWL = BLOCK_ENTITY_TYPE.register(
            "soup_bowl", basicBlockEntityType(SoupBowlBlockEntity::new, ModBlockRegistry.SOUP_BOWL));
    public static final Supplier<BlockEntityType<BrewingBarrelBlockEntity>> BREWING_BARREL = BLOCK_ENTITY_TYPE.register(
            "brewing_barrel", basicBlockEntityType(BrewingBarrelBlockEntity::new, ModBlockRegistry.BREWING_BARREL));
    public static final Supplier<BlockEntityType<SaltPanBlockEntity>> SALT_PAN = BLOCK_ENTITY_TYPE.register(
            "salt_pan", basicBlockEntityType(SaltPanBlockEntity::new, ModBlockRegistry.SALT_PAN));
}
