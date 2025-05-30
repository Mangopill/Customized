package mangopill.customized.common.block;

import mangopill.customized.common.block.entity.ModBrushableBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BrushableBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ModBrushableBlock extends BrushableBlock {

    public ModBrushableBlock(Block turnsInto, Properties properties, SoundEvent brushSound, SoundEvent brushCompletedSound) {
        super(turnsInto, properties, brushSound, brushCompletedSound);
    }
    @Nullable
    @Override
    public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
        return new ModBrushableBlockEntity(pos, state);
    }
}
