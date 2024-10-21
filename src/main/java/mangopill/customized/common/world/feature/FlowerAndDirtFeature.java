package mangopill.customized.common.world.feature;

import com.mojang.serialization.Codec;
import mangopill.customized.common.registry.ModBlcokRegistry;
import mangopill.customized.common.registry.ModBlockEntityTypeRegistry;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.predicate.BlockStatePredicate;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;

public class FlowerAndDirtFeature extends Feature<NoneFeatureConfiguration> {

    private static final BlockStatePredicate IS_DIRT = BlockStatePredicate.forBlock(Blocks.DIRT);
    private static final BlockStatePredicate IS_GRASS_BLOCK = BlockStatePredicate.forBlock(Blocks.GRASS_BLOCK);
    private static final BlockStatePredicate IS_COARSE_DIRT = BlockStatePredicate.forBlock(Blocks.COARSE_DIRT);
    private static final BlockStatePredicate IS_PODZOL = BlockStatePredicate.forBlock(Blocks.PODZOL);
    private static final BlockStatePredicate IS = BlockStatePredicate.forBlock(Blocks.GRASS_BLOCK);
    private final BlockState blue_orchid = Blocks.BLUE_ORCHID.defaultBlockState();

    public FlowerAndDirtFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel worldgenlevel = context.level();
        BlockPos blockpos = context.origin();
        blockpos = blockpos.above();
        while (worldgenlevel.isEmptyBlock(blockpos) && blockpos.getY() > worldgenlevel.getMinBuildHeight() + 2) {
            blockpos = blockpos.below();
        }
        if (IS_GRASS_BLOCK.test(worldgenlevel.getBlockState(blockpos))
                || IS_GRASS_BLOCK.test(worldgenlevel.getBlockState(blockpos))
                || IS_COARSE_DIRT.test(worldgenlevel.getBlockState(blockpos))
                || IS_PODZOL.test(worldgenlevel.getBlockState(blockpos))
                && IS_DIRT.test(worldgenlevel.getBlockState(blockpos.below()))) {
            worldgenlevel.setBlock(blockpos.above(), this.blue_orchid, 2);
            placeSusDirt(worldgenlevel, blockpos.below());
            return true;
        } else return false;
    }

    private static void placeSusDirt(WorldGenLevel level, BlockPos pos) {
        level.setBlock(pos, ModBlcokRegistry.SUSPICIOUS_DIRT.get().defaultBlockState(), 3);
        level.getBlockEntity(pos, ModBlockEntityTypeRegistry.SUSPICIOUS_DIRT.get())
                .ifPresent(Consumer -> Consumer.setLootTable(BuiltInLootTables.DESERT_WELL_ARCHAEOLOGY, pos.asLong()));
    }

}
