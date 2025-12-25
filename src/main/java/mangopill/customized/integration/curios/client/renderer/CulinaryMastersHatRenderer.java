package mangopill.customized.integration.curios.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import mangopill.customized.client.util.strategy.RenderHatModelStrategy;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;

import static mangopill.customized.client.util.ClientUtil.*;
import static mangopill.customized.common.util.CStringUtil.*;

public class CulinaryMastersHatRenderer implements ICurioRenderer {

    public CulinaryMastersHatRenderer() {
    }

    @Override
    public <T extends LivingEntity, M extends EntityModel<T>> void render(ItemStack stack, SlotContext slotContext, PoseStack poseStack, RenderLayerParent<T, M> renderLayerParent, MultiBufferSource buffer, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        renderItemStackByStrategy(poseStack, buffer, light, OverlayTexture.NO_OVERLAY,
                new RenderHatModelStrategy<>(renderLayerParent.getModel(), stack, 1.0F, -1.0F, getCLoc("item/curio/culinary_masters_hat")));
    }
}
