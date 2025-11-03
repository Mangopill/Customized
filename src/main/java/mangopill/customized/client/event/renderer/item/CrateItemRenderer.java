package mangopill.customized.client.event.renderer.item;

import com.mojang.blaze3d.vertex.PoseStack;
import mangopill.customized.common.item.CrateItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

import java.util.List;

import static mangopill.customized.client.util.ClientUtil.*;

public class CrateItemRenderer extends BlockEntityWithoutLevelRenderer {
    public CrateItemRenderer() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {
        if (stack.getItem() instanceof CrateItem crateItem){
            List<ItemStack> stackList = crateItem.getItemStackListInCrate(stack);
            if (Minecraft.getInstance().player != null) {
                renderModel(stack, displayContext, poseStack, buffer, light, overlay, "item/crate_renderer");
                stackList.forEach(itemStack -> renderItemStack(itemStack, poseStack, buffer, null, light, overlay, 0.5F, 0.5F, 0.95F, 0.6F, 0.6F, 0.6F, 0.0F, 0.0F, 0.0F));
            }
        }
    }

    public static class CrateItemExtensions implements IClientItemExtensions {
        private final BlockEntityWithoutLevelRenderer renderer = new CrateItemRenderer();
        @Override
        public BlockEntityWithoutLevelRenderer getCustomRenderer() {
            return renderer;
        }
    }
}
