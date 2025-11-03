package mangopill.customized.client.event.renderer.item;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.world.item.*;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

import static mangopill.customized.client.util.ClientUtil.*;

public class SoupBowlItemRenderer extends BlockEntityWithoutLevelRenderer {
    public SoupBowlItemRenderer() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {
        renderModel(stack, displayContext, poseStack, buffer, light, overlay, "item/soup_bowl_with_drive_renderer");
        renderDrivePlateItem(stack, poseStack, buffer, light, overlay, 0.295F, 0.295F, 0.25F, 0.695F, 0.695F, 0.3125F);
    }

    public static class SoupBowlItemExtensions implements IClientItemExtensions {
        private final BlockEntityWithoutLevelRenderer renderer = new SoupBowlItemRenderer();
        @Override
        public BlockEntityWithoutLevelRenderer getCustomRenderer() {
            return renderer;
        }
    }
}
