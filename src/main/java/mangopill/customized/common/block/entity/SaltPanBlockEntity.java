package mangopill.customized.common.block.entity;

import mangopill.customized.common.block.handler.PotFluidHandler;
import mangopill.customized.common.registry.ModBlockEntityTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SaltPanBlockEntity extends BlockEntity {
    private final LazyOptional<PotFluidHandler> fluidHandler;

    public SaltPanBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypeRegistry.SALT_PAN.get(), pos, state);
        this.fluidHandler = LazyOptional.of(() ->  new PotFluidHandler(level, pos));
    }

    @Override
    public <T> @Nonnull LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.FLUID_HANDLER) {
            return fluidHandler.cast();
        }
        return super.getCapability(cap, side);
    }
}
