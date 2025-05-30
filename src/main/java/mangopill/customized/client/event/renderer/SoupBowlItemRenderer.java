package mangopill.customized.client.event.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.world.item.*;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import javax.annotation.Nonnull;

import static mangopill.customized.client.util.ClientUtil.*;

public class SoupBowlItemRenderer extends BlockEntityWithoutLevelRenderer {
    public SoupBowlItemRenderer() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
    }

    @Override
    public void renderByItem(@Nonnull ItemStack stack, @Nonnull ItemDisplayContext displayContext, @Nonnull PoseStack poseStack, @Nonnull MultiBufferSource multiBufferSource, int light, int overlay) {
        renderModel(stack, displayContext, poseStack, multiBufferSource, light, overlay);
        renderDrivePlateItem(stack, poseStack, multiBufferSource, light, overlay, 0.295F, 0.295F, 0.25F, 0.695F, 0.695F, 0.3125F);
    }

    public static class SoupBowlItemExtensions implements IClientItemExtensions {
        private final BlockEntityWithoutLevelRenderer renderer = new SoupBowlItemRenderer();
        @Override
        public @Nonnull BlockEntityWithoutLevelRenderer getCustomRenderer() {
            return renderer;
        }
    }
}
