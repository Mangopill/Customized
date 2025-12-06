package mangopill.customized.common.registry;

import mangopill.customized.Customized;
import mangopill.customized.common.block.entity.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.*;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static mangopill.customized.common.util.RegistryUtil.*;

public class CBlockEntityTypeRegistry {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPE = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Customized.MODID);
    //block
    public static final Supplier<BlockEntityType<CBrushableBlockEntity>> SUSPICIOUS_DIRT = BLOCK_ENTITY_TYPE.register(
            "suspicious_dirt", basicBlockEntityType(CBrushableBlockEntity::new, CBlockRegistry.SUSPICIOUS_DIRT));
    //kitchenware block
    public static final Supplier<BlockEntityType<CasseroleBlockEntity>> CASSEROLE = BLOCK_ENTITY_TYPE.register(
            "casserole", basicBlockEntityType(CasseroleBlockEntity::new, CBlockRegistry.CASSEROLE));
    public static final Supplier<BlockEntityType<RoasterBlockEntity>> ROASTER = BLOCK_ENTITY_TYPE.register(
            "roaster", basicBlockEntityType(RoasterBlockEntity::new, CBlockRegistry.ROASTER));
    public static final Supplier<BlockEntityType<SoupBowlBlockEntity>> SOUP_BOWL = BLOCK_ENTITY_TYPE.register(
            "soup_bowl", basicBlockEntityType(SoupBowlBlockEntity::new, CBlockRegistry.SOUP_BOWL));
    public static final Supplier<BlockEntityType<BakingPanBlockEntity>> BAKING_PAN = BLOCK_ENTITY_TYPE.register(
            "baking_pan", basicBlockEntityType(BakingPanBlockEntity::new, CBlockRegistry.BAKING_PAN));
    public static final Supplier<BlockEntityType<BrewingBarrelBlockEntity>> BREWING_BARREL = BLOCK_ENTITY_TYPE.register(
            "brewing_barrel", basicBlockEntityType(BrewingBarrelBlockEntity::new, CBlockRegistry.BREWING_BARREL));
    public static final Supplier<BlockEntityType<CrateBlockEntity>> CRATE = BLOCK_ENTITY_TYPE.register(
            "crate", basicBlockEntityType(CrateBlockEntity::new, CBlockRegistry.CRATE));
    public static final Supplier<BlockEntityType<CuttingBoardBlockEntity>> CUTTING_BOARD = BLOCK_ENTITY_TYPE.register(
            "cutting_board", basicBlockEntityType(CuttingBoardBlockEntity::new, CBlockRegistry.CUTTING_BOARD));
}
