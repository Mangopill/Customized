package mangopill.customized.common.world.feature;

import com.mojang.serialization.Codec;
import mangopill.customized.Customized;
import mangopill.customized.common.registry.ModBlcokRegistry;
import mangopill.customized.common.registry.ModBlockEntityTypeRegistry;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.predicate.BlockStatePredicate;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.storage.loot.LootTable;

public class FlowerAndDirtFeature extends Feature<NoneFeatureConfiguration> {
    private static final ResourceKey<LootTable> LOOT_TABLE = ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(Customized.MODID, "archaeology/flower_and_dirt"));
    private static final BlockStatePredicate IS_GRASS_BLOCK = BlockStatePredicate.forBlock(Blocks.GRASS_BLOCK);
    private final BlockState BLUE_ORCHID = Blocks.BLUE_ORCHID.defaultBlockState();

    public FlowerAndDirtFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel worldgenlevel = context.level();
        BlockPos blockpos = context.origin().below();
        if (IS_GRASS_BLOCK.test(worldgenlevel.getBlockState(blockpos))) {
            worldgenlevel.setBlock(blockpos.above(), this.BLUE_ORCHID, 2);
            placeSusDirt(worldgenlevel, blockpos.below());
            return true;
        } else return false;
    }

    private static void placeSusDirt(WorldGenLevel level, BlockPos pos) {
        level.setBlock(pos, ModBlcokRegistry.SUSPICIOUS_DIRT.get().defaultBlockState(), 3);
        level.getBlockEntity(pos, ModBlockEntityTypeRegistry.SUSPICIOUS_DIRT.get())
                .ifPresent(Consumer -> Consumer.setLootTable(LOOT_TABLE, pos.asLong()));
    }

}
