package mangopill.customized.client.event.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemDisplayContext;

public class AbstractArrowItemRenderer<T extends AbstractArrow> extends ArrowRenderer<T> {
    private final float scale;
    private final ItemRenderer itemRenderer;

    public AbstractArrowItemRenderer(EntityRendererProvider.Context context) {
        this(context, 1.0F);
    }

    public AbstractArrowItemRenderer(EntityRendererProvider.Context context, float scale) {
        super(context);
        this.scale = scale;
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(T entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        if (entity.tickCount >= 2 || !(this.entityRenderDispatcher.camera.getEntity().distanceToSqr(entity) < (double) 12.25F)) {
            poseStack.pushPose();
            poseStack.scale(scale, scale, scale);
            poseStack.mulPose(Axis.YP.rotationDegrees(entity.getYRot() - 90.0F));
            poseStack.mulPose(Axis.ZP.rotationDegrees(-90.0F));
            itemRenderer.renderStatic(entity.getPickupItemStackOrigin(), ItemDisplayContext.GROUND, packedLight, OverlayTexture.NO_OVERLAY, poseStack, buffer, entity.level(), entity.getId());
            poseStack.popPose();
        }
    }

    @Override
    public ResourceLocation getTextureLocation(T t) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}
