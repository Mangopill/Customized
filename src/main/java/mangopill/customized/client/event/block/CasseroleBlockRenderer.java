package mangopill.customized.client.event.block;

import com.mojang.blaze3d.vertex.*;
import mangopill.customized.client.util.strategy.*;
import mangopill.customized.common.block.AbstractPotBlock;
import mangopill.customized.common.block.entity.AbstractPotBlockEntity;
import mangopill.customized.common.block.state.PotState;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.ItemStack;

import java.util.List;

import static mangopill.customized.client.event.item.SoupBowlItemRenderer.*;
import static mangopill.customized.client.util.ClientUtil.*;
import static net.minecraft.client.renderer.RenderStateShard.*;

public class CasseroleBlockRenderer implements BlockEntityRenderer<AbstractPotBlockEntity> {

    public CasseroleBlockRenderer(BlockEntityRendererProvider.Context context) {
    }

    public static final RenderType ITEM_IN_FLUID_WITH_OFFSET = RenderType.create(
            "item_in_fluid_with_offset",
            DefaultVertexFormat.BLOCK,
            VertexFormat.Mode.QUADS,
            256,
            true,
            true,
            RenderType.CompositeState.builder()
                    .setShaderState(RENDERTYPE_CUTOUT_SHADER)
                    .setTextureState(BLOCK_SHEET)
                    .setLightmapState(LIGHTMAP)
                    .setLayeringState(C_POLYGON_OFFSET_LAYERING)
                    .setDepthTestState(LEQUAL_DEPTH_TEST)
                    .setWriteMaskState(COLOR_DEPTH_WRITE)
                    .createCompositeState(true)
    );

    @Override
    public void render(AbstractPotBlockEntity blockEntity, float v, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {
        if (blockEntity.getBlockState().getValue(AbstractPotBlock.LID).equals(PotState.WITH_LID)) return;
        List<ItemStack> stackList = blockEntity.getItemStackListInPot(false, false);
        boolean dynamic = blockEntity.getBlockState().getValue(AbstractPotBlock.LID).equals(PotState.WITH_DRIVE);
        renderBlockEntityByStrategy(blockEntity, poseStack, buffer, light, overlay,
                new RenderItemListDriveStrategy(blockEntity.getLevel(), stackList,
                        0.22F, 0.22F, 0.3F, 0.78F, 0.78F, 0.4678F,
                        ITEM_IN_FLUID_WITH_OFFSET, dynamic),
                new RenderFluidInBlockStrategy(blockEntity.getLevel(), blockEntity.getBlockPos(),
                        blockEntity.getFluidHandler(), stackList,
                        0.125F, 0.188F, 0.125F, 0.875F, 0.5F, 0.875F, false));
    }
}