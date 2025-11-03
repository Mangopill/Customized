package mangopill.customized.client.event.renderer.player;

import com.mojang.blaze3d.vertex.PoseStack;
import mangopill.customized.common.item.ModHatItem;
import net.minecraft.client.model.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import static mangopill.customized.client.util.ClientUtil.renderHatModel;

public class CHatLayerRenderer<T extends LivingEntity, M extends HumanoidModel<T>> extends RenderLayer<T, M> {
    public CHatLayerRenderer(RenderLayerParent<T, M> context) {
        super(context);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, T entity, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        ItemStack stack = entity.getItemBySlot(EquipmentSlot.HEAD);
        if (stack.getItem() instanceof ModHatItem hat) {
            renderHatModel(poseStack, buffer, packedLight, this.getParentModel(), stack, hat.getScale(), hat.getTranslateY(), "item/armor/");
        }
    }
}