package mangopill.customized.client.event.player;

import com.mojang.blaze3d.vertex.PoseStack;
import mangopill.customized.client.util.strategy.RenderHatModelStrategy;
import mangopill.customized.common.item.CHatItem;
import net.minecraft.client.model.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import static mangopill.customized.client.util.ClientUtil.*;

public class CHatLayerRenderer<T extends LivingEntity, M extends HumanoidModel<T>> extends RenderLayer<T, M> {

    public CHatLayerRenderer(RenderLayerParent<T, M> context) {
        super(context);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int light, T entity, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        ItemStack stack = entity.getItemBySlot(EquipmentSlot.HEAD);
        if (!(stack.getItem() instanceof CHatItem hat)) return;
        renderItemStackByStrategy(poseStack, buffer, light, OverlayTexture.NO_OVERLAY,
                new RenderHatModelStrategy<>(getParentModel(), stack, hat.getScale(), hat.getTranslateY(), hat.getTexture()));
    }
}