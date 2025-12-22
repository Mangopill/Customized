package mangopill.customized.client.event.item;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import mangopill.customized.client.util.strategy.*;
import mangopill.customized.common.item.AbstractPlateItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.world.item.*;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

import java.util.List;

import static mangopill.customized.client.util.ClientUtil.*;
import static mangopill.customized.common.util.StringUtil.*;
import static net.minecraft.client.renderer.RenderStateShard.*;

public class SoupBowlItemRenderer extends BlockEntityWithoutLevelRenderer {

    public SoupBowlItemRenderer() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
    }

    public static final RenderStateShard.LayeringStateShard C_POLYGON_OFFSET_LAYERING =
            new LayeringStateShard("c_polygon_offset_layering", () -> {
                RenderSystem.polygonOffset(0.0F, -3.0F);
                RenderSystem.enablePolygonOffset();
            }, () -> {
                RenderSystem.polygonOffset(0.0F, 0.0F);
                RenderSystem.disablePolygonOffset();
            });

    public static final RenderType ITEM_IN_FLUID_WITH_OFFSET = RenderType.create(
            "item_in_fluid_with_offset",
            DefaultVertexFormat.NEW_ENTITY,
            VertexFormat.Mode.QUADS,
            256,
            true,
            true,
            RenderType.CompositeState.builder()
                    .setShaderState(RENDERTYPE_ENTITY_TRANSLUCENT_SHADER)
                    .setTextureState(BLOCK_SHEET_MIPPED)
                    .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                    .setCullState(NO_CULL)
                    .setLightmapState(LIGHTMAP)
                    .setOverlayState(OVERLAY)
                    .setLayeringState(C_POLYGON_OFFSET_LAYERING)
                    .setDepthTestState(LEQUAL_DEPTH_TEST)
                    .setWriteMaskState(COLOR_DEPTH_WRITE)
                    .createCompositeState(true)
    );

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {
        if (!(stack.getItem() instanceof AbstractPlateItem plateItem)) return;
        List<ItemStack> stackList = plateItem.getItemStackListInPlate(stack, false);
        if (Minecraft.getInstance().player == null) return;
        renderItemStackByStrategy(poseStack, buffer, light, overlay,
                new RenderItemListDriveStrategy(Minecraft.getInstance().player.level(), stackList,
                        0.295F, 0.295F, 0.25F, 0.695F, 0.695F, 0.3125F,
                        ITEM_IN_FLUID_WITH_OFFSET, true),
                new RenderCustomItemModelStrategy(stack, ItemDisplayContext.NONE, getCLoc("item/soup_bowl_with_drive_renderer"), false));
    }

    public static class SoupBowlItemExtensions implements IClientItemExtensions {
        private final BlockEntityWithoutLevelRenderer renderer = new SoupBowlItemRenderer();
        @Override
        public BlockEntityWithoutLevelRenderer getCustomRenderer() {
            return renderer;
        }
    }
}
