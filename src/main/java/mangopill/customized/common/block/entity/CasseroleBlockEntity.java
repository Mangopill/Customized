package mangopill.customized.common.block.entity;

import mangopill.customized.common.registry.ModBlockEntityTypeRegistry;
import mangopill.customized.common.registry.ModParticleTypeRegistry;
import mangopill.customized.common.registry.ModRecipeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class CasseroleBlockEntity extends AbstractPotBlockEntity{
    public CasseroleBlockEntity(BlockPos pos, BlockState blockState) {
        super(getBlockEntityType(), pos, blockState, 6, 6, RecipeManager.createCheck(ModRecipeRegistry.CASSEROLE.get()));
    }

    public static BlockEntityType<? extends AbstractPotBlockEntity> getBlockEntityType() {
        return ModBlockEntityTypeRegistry.CASSEROLE.get();
    }

    @Override
    public void particleTick(Level level, BlockPos pos, AbstractPotBlockEntity potBlockEntity) {
        RandomSource random = level.random;
        if (random.nextFloat() < 0.4F) {
            double x = (double) pos.getX() + 0.4D + (random.nextDouble() * 0.5D - 0.3D);
            double y = (double) pos.getY() + 0.4D;
            double z = (double) pos.getZ() + 0.4D + (random.nextDouble() * 0.5D - 0.3D);
            level.addParticle(ParticleTypes.BUBBLE_POP, x, y, z, 0.0D, 0.0D, 0.0D);
        }
        if (random.nextFloat() < 0.3F) {
            double x = (double) pos.getX() + 0.4D + (random.nextDouble() * 0.6D - 0.2D);
            double y = (double) pos.getY() + 0.4D;
            double z = (double) pos.getZ() + 0.4D + (random.nextDouble() * 0.6D - 0.2D);
            level.addParticle(ModParticleTypeRegistry.STEAM.get(), x, y, z, 0.0D, 0.0D, 0.0D);
        }
    }
}
