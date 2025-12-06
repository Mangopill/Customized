package mangopill.customized.common.block.entity;

import mangopill.customized.common.block.record.PotRecord;
import mangopill.customized.common.item.AbstractPlateItem;
import mangopill.customized.common.registry.CParticleTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class CasseroleBlockEntity extends AbstractPotBlockEntity{
    public CasseroleBlockEntity(BlockPos pos, BlockState blockState) {
        super(PotRecord.CASSEROLE.entityType(), pos, blockState, PotRecord.CASSEROLE.ingredientCount(), PotRecord.CASSEROLE.seasoningCount(), PotRecord.CASSEROLE.spiceCount(), PotRecord.CASSEROLE.potCheck());
    }

    @Override
    public AbstractPlateItem getPlateItem() {
        return PotRecord.CASSEROLE.plateItem();
    }

    @Override
    public void particleTick(Level level, BlockPos pos, AbstractPotBlockEntity potBlockEntity) {
        addSimpleParticle(level, pos, ParticleTypes.BUBBLE_POP,0.4F, 0.1F, 0.9F, 0.5F, 0.1F, 0.9F);
        addSimpleParticle(level, pos, CParticleTypeRegistry.STEAM.get(),0.3F, 0.1F, 0.9F, 0.5F, 0.1F, 0.9F);
    }
}
