package mangopill.customized.client.event.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mangopill.customized.Customized;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;

import static mangopill.customized.client.util.ClientUtil.renderDrivePlateItem;
import static net.minecraft.client.renderer.entity.ItemRenderer.*;

public class SoupBowlItemRenderer extends BlockEntityWithoutLevelRenderer {
    public SoupBowlItemRenderer() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
    }

    @Override
    public void renderByItem(@NotNull ItemStack stack, @NotNull ItemDisplayContext displayContext, @NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int light, int overlay) {
        BakedModel model = Minecraft.getInstance().getItemRenderer()
                .getItemModelShaper().getModelManager().getModel(ModelResourceLocation.standalone(
                        ResourceLocation.fromNamespaceAndPath(Customized.MODID, "item/soup_bowl_with_drive_renderer")
                ));
        boolean flag1;
        label78:
        {
            if (displayContext != ItemDisplayContext.GUI && !displayContext.firstPerson()) {
                Item item = stack.getItem();
                if (item instanceof BlockItem blockItem) {
                    Block block = blockItem.getBlock();
                    flag1 = !(block instanceof HalfTransparentBlock) && !(block instanceof StainedGlassPaneBlock);
                    break label78;
                }
            }
            flag1 = true;
        }
        for (BakedModel passModel : model.getRenderPasses(stack, flag1)) {
            for (RenderType renderType : passModel.getRenderTypes(stack, flag1)) {
                VertexConsumer vertexConsumer;
                if (flag1) {
                    vertexConsumer = getFoilBufferDirect(multiBufferSource, renderType, true, stack.hasFoil());
                } else {
                    vertexConsumer = getFoilBuffer(multiBufferSource, renderType, true, stack.hasFoil());
                }
                Minecraft.getInstance().getItemRenderer().renderModelLists(passModel, stack, light, overlay, poseStack, vertexConsumer);
            }
        }
        renderDrivePlateItem(stack, poseStack, multiBufferSource, light, overlay, 0.1F, 0.25F, 0.2F, 0.75F, 0.75F, 0.45F);
    }

    public static class SoupBowlItemExtensions implements IClientItemExtensions {
        private final BlockEntityWithoutLevelRenderer renderer = new SoupBowlItemRenderer();
        @Override
        public @NotNull BlockEntityWithoutLevelRenderer getCustomRenderer() {
            return renderer;
        }
    }
}
