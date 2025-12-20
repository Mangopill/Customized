package mangopill.customized.client.event.item;

import com.mojang.blaze3d.vertex.PoseStack;
import mangopill.customized.client.util.strategy.*;
import mangopill.customized.common.item.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

import java.util.List;

import static mangopill.customized.client.util.ClientUtil.*;
import static mangopill.customized.common.util.StringUtil.*;

public class CrateItemRenderer extends BlockEntityWithoutLevelRenderer {

    public CrateItemRenderer() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {
        if (!(stack.getItem() instanceof CrateItem crateItem)) return;
        List<ItemStack> stackList = crateItem.getItemStackListInCrate(stack);
        renderItemStackByStrategy(poseStack, buffer, light, overlay, new RenderCustomItemModelStrategy(stack, ItemDisplayContext.NONE, getCLoc("item/crate_renderer"), false));
        if (stackList.isEmpty()) return;
        renderItemStackByStrategy(poseStack, buffer, light, overlay,
                new RenderSimpleAdjustItemStack(stackList.getFirst(), null, 0.5F, 0.5F, 0.95F,
                        0.6F, 0.6F, 0.6F, 0.0F, 0.0F, 0.0F));
    }

    public static class CrateItemExtensions implements IClientItemExtensions {
        private final BlockEntityWithoutLevelRenderer renderer = new CrateItemRenderer();
        @Override
        public BlockEntityWithoutLevelRenderer getCustomRenderer() {
            return renderer;
        }
    }
}
