package mangopill.customized.common.block.entity;

import mangopill.customized.common.block.record.PotRecord;
import mangopill.customized.common.registry.CParticleTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import static mangopill.customized.common.util.SensoryUtil.*;

public class WokBlockEntity extends AbstractPotBlockEntity{

    public WokBlockEntity(BlockPos pos, BlockState blockState) {
        super(pos, blockState, PotRecord.WOK);
    }

    @Override
    public void particleTick(Level level, BlockPos pos, AbstractPotBlockEntity potBlockEntity) {
        RandomSource random = RandomSource.create();
        addRandomParticle(level, pos, ParticleTypes.BUBBLE_POP, random,0.4F, 1, 0.1D, 0.9D, 0.5D, 0.1D, 0.9D);
        addRandomParticle(level, pos, CParticleTypeRegistry.STEAM.get(), random,0.3F, 1, 0.1D, 0.9D, 0.5D, 0.1D, 0.9D);
    }
}
