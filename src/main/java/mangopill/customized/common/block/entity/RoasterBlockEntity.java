package mangopill.customized.common.block.entity;

import mangopill.customized.common.block.record.PotRecord;
import mangopill.customized.common.block.state.PotState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import static mangopill.customized.common.block.AbstractPotBlock.*;
import static mangopill.customized.common.util.SensoryUtil.*;

public class RoasterBlockEntity extends AbstractPotBlockEntity{

    public RoasterBlockEntity(BlockPos pos, BlockState blockState) {
        super(pos, blockState, PotRecord.ROASTER);
    }

    @Override
    public void particleTick(Level level, BlockPos pos, AbstractPotBlockEntity potBlockEntity) {
        RandomSource random = RandomSource.create();
        addRandomParticle(level, pos, ParticleTypes.SMOKE, random,0.4F, 1, 0.0F, 1.0F, 0.4F, 0.0F, 1.0F);
    }

    @Override
    public boolean isHeated() {
        return level != null && !level.getBlockState(worldPosition).getValue(LID).equals(PotState.WITHOUT_LID);
    }
}
