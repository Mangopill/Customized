package mangopill.customized.common.registry;

import mangopill.customized.Customized;
import mangopill.customized.common.block.ModBrushableBlock;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static net.minecraft.world.level.block.Blocks.DIRT;

public class ModBlcokRegistry {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, Customized.MODID);

    public static final Supplier<Block> SUSPICIOUS_DIRT = BLOCKS.register(
            "suspicious_dirt",
            () -> new ModBrushableBlock(
                    DIRT,
                    SoundEvents.BRUSH_GRAVEL,
                    SoundEvents.BRUSH_GRAVEL_COMPLETED,
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.DIRT)
                            .instrument(NoteBlockInstrument.SNARE)
                            .strength(0.25F)
                            .sound(SoundType.SUSPICIOUS_GRAVEL)
                            .pushReaction(PushReaction.DESTROY))
            );
}