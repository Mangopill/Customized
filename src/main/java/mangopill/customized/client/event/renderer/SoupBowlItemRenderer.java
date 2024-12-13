package mangopill.customized.client.event.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.world.item.*;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;

import static mangopill.customized.client.util.ClientUtil.*;

public class SoupBowlItemRenderer extends BlockEntityWithoutLevelRenderer {
    public SoupBowlItemRenderer() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
    }

    @Override
    public void renderByItem(@NotNull ItemStack stack, @NotNull ItemDisplayContext displayContext, @NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int light, int overlay) {
        renderModel(stack, displayContext, poseStack, multiBufferSource, light, overlay);
        renderDrivePlateItem(stack, poseStack, multiBufferSource, light, overlay, 0.1F, 0.25F, 0.2F, 0.75F, 0.75F, 0.4F);
    }

    public static class SoupBowlItemExtensions implements IClientItemExtensions {
        private final BlockEntityWithoutLevelRenderer renderer = new SoupBowlItemRenderer();
        @Override
        public @NotNull BlockEntityWithoutLevelRenderer getCustomRenderer() {
            return renderer;
        }
    }
}
