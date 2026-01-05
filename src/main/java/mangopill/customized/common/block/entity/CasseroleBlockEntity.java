package mangopill.customized.common.block.entity;

import mangopill.customized.common.block.AbstractPotBlock;
import mangopill.customized.common.block.record.PotRecord;
import mangopill.customized.common.block.state.PotState;
import mangopill.customized.common.registry.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.List;

import static mangopill.customized.common.util.SensoryUtil.*;
import static mangopill.customized.common.util.PropertyValueUtil.*;

public class CasseroleBlockEntity extends AbstractPotBlockEntity{

    public CasseroleBlockEntity(BlockPos pos, BlockState blockState) {
        super(pos, blockState, PotRecord.CASSEROLE);
    }

    @Override
    protected void cookCustomized(Level level, BlockState state) {
        if (fluidHandler != null && !fluidHandler.isWaterOrSoup()) return;
        ++customizedTime;
        lidAccelerate(state);
        getCustomizedCookingCompletionTime();
        if (customizedTime < customizedCompletionTime) return;
        setSoup(level, getItemStackListInPot(false, true));
        if (itemStackHandler.getStackInSlot(ingredientInput + seasoningInput + spiceInput).isEmpty()) return;
        transferAndSpawn(level, getItemStackListInPot(false, true));
        clearCustomizedTime();
    }

    public void setSoup(Level level, List<ItemStack> stackList) {
        if (fluidHandler == null) return;
        DataComponentPatch patch = DataComponentPatch.builder().set(CDataComponentRegistry.ARGB_COLOR.get(), getMaxValueColor(level, stackList)).build();
        fluidHandler.setStoredFluidDirectly(new FluidStack(CFluidRegistry.SOUP.get().defaultFluidState().holder(), fluidHandler.getStoredFluid().getAmount(), patch));
    }

    @Override
    public void particleTick(Level level, BlockPos pos, AbstractPotBlockEntity potBlockEntity) {
        RandomSource random = RandomSource.create();
        if (potBlockEntity.getBlockState().getValue(AbstractPotBlock.LID).equals(PotState.WITH_DRIVE) && potBlockEntity.getFluidHandler().isWaterOrSoup()) {
            addRandomParticle(level, pos, ParticleTypes.BUBBLE_POP, random, 0.4F, 1, 0.1D, 0.9D, 0.5D, 0.1D, 0.9D);
        }
        addRandomParticle(level, pos, CParticleTypeRegistry.STEAM.get(), random,0.3F, 1, 0.1D, 0.9D, 0.5D, 0.1D, 0.9D);
    }
}
