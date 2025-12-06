package mangopill.customized.client.event.item;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.world.item.*;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

import static mangopill.customized.client.util.ClientUtil.*;
import static mangopill.customized.common.util.StringUtil.*;

public class BakingPanItemRenderer extends BlockEntityWithoutLevelRenderer {
    public BakingPanItemRenderer() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {
        renderLayeredPlateItem(stack, poseStack, buffer, light, overlay, 0.28F, 0.28F, 0.05F, 0.72F, 0.72F, 0.1F, getCLoc("item/baking_pan_with_drive_renderer"));
    }

    public static class BakingPanItemExtensions implements IClientItemExtensions {
        private final BlockEntityWithoutLevelRenderer renderer = new BakingPanItemRenderer();
        @Override
        public BlockEntityWithoutLevelRenderer getCustomRenderer() {
            return renderer;
        }
    }
}
