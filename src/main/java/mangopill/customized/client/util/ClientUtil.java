package mangopill.customized.client.util;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import mangopill.customized.Customized;
import mangopill.customized.common.block.entity.AbstractPlateBlockEntity;
import mangopill.customized.common.block.entity.AbstractPotBlockEntity;
import mangopill.customized.common.item.AbstractPlateItem;
import mangopill.customized.common.util.ModItemStackHandlerHelper;
import mangopill.customized.common.util.category.NutrientCategory;
import mangopill.customized.common.util.value.PropertyValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static net.minecraft.client.renderer.entity.ItemRenderer.getFoilBuffer;
import static net.minecraft.client.renderer.entity.ItemRenderer.getFoilBufferDirect;

public final class ClientUtil {
    private ClientUtil() {
    }
    public static int getMaxValueColor(Level level, List<ItemStack> stackList) {
        Map<NutrientCategory, Float> nutrientSums = new HashMap<>();
        for (ItemStack stack : stackList) {
            @NotNull PropertyValue propertyValue = ModItemStackHandlerHelper.getPropertyValue(stack, level);
            if (!propertyValue.isEmpty()) {
                for (Pair<NutrientCategory, Float> entry : propertyValue.toSet()) {
                    NutrientCategory category = entry.getKey();
                    float value = entry.getValue() * stack.getCount();
                    nutrientSums.put(category, nutrientSums.getOrDefault(category, 0f) + value);
                }
            }
        }
        NutrientCategory maxCategory = null;
        double maxSum = 0D;
        for (Map.Entry<NutrientCategory, Float> entry : nutrientSums.entrySet()) {
            if (entry.getValue() > maxSum) {
                maxSum = entry.getValue();
                maxCategory = entry.getKey();
            }
        }
        return maxCategory != null ? maxCategory.getColor().getValue() : 0x3F76E4;
    }

    public static void renderDrivePot(AbstractPotBlockEntity potBlockEntity, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay,
                                      float startLength, float startWidth, float startHeight, float endLength, float endWidth, float endHeight) {
        List<ItemStack> stackList = potBlockEntity.getItemStackListInPot(true, false);
        renderDrive(potBlockEntity.getLevel(), stackList, poseStack, buffer, light, overlay, startLength, startWidth, startHeight, endLength, endWidth, endHeight);
    }

    public static void renderDrivePlate(AbstractPlateBlockEntity plateBlockEntity, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay,
                                        float startLength, float startWidth, float startHeight, float endLength, float endWidth, float endHeight) {
        List<ItemStack> stackList = plateBlockEntity.getItemStackListInPlate(false);
        renderDrive(plateBlockEntity.getLevel(), stackList, poseStack, buffer, light, overlay, startLength, startWidth, startHeight, endLength, endWidth, endHeight);
    }

    public static void renderDrivePlateItem(ItemStack stack, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay,
                                        float startLength, float startWidth, float startHeight, float endLength, float endWidth, float endHeight) {
        if (stack.getItem() instanceof AbstractPlateItem plateItem){
            List<ItemStack> stackList = plateItem.getItemStackListInPlate(stack, false);
            if (Minecraft.getInstance().player != null) {
                renderDrive(Minecraft.getInstance().player.level(), stackList, poseStack, buffer, light, overlay, startLength, startWidth, startHeight, endLength, endWidth, endHeight);
            }
        }
    }

    public static void renderDrive(Level level, List<ItemStack> stackList, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay,
                                      float startLength, float startWidth, float startHeight, float endLength, float endWidth, float endHeight) {
        for (ItemStack stack : stackList) {
            ItemStack newStack = stack.copy();
            if (!newStack.isEmpty()) {
                int count = newStack.getCount();
                int renderCount = count / 32;
                if (count % 32 > 0) {
                    renderCount++;
                }
                for (int i = 0; i < renderCount; i++) {
                    Random rand = new Random(stackList.lastIndexOf(stack));
                    int seed = Item.getId(newStack.getItem()) + rand.nextInt() + i;
                    Random random = new Random(seed);
                    float randX = startLength + (random.nextFloat(endLength - startLength));
                    float randY = startHeight + (random.nextFloat(endHeight - startHeight));
                    float randZ = startWidth + (random.nextFloat(endWidth - startWidth));
                    poseStack.pushPose();
                    poseStack.translate(randX, randY, randZ);
                    poseStack.mulPose(Axis.ZP.rotationDegrees(random.nextFloat(360)));
                    poseStack.mulPose(Axis.XP.rotationDegrees(random.nextFloat(360)));
                    poseStack.mulPose(Axis.YP.rotationDegrees(random.nextFloat(360)));
                    poseStack.scale(0.3F, 0.3F, 0.3F);
                    if (level != null) {
                        ItemStack renderItemStack = newStack.copy();
                        renderItemStack.setCount(1);
                        Minecraft.getInstance().getItemRenderer().renderStatic(renderItemStack, ItemDisplayContext.FIXED, light, overlay, poseStack, buffer, null, seed);
                    }
                    poseStack.popPose();
                }
            }
        }
    }

    public static void renderDefault(@NotNull ItemStack stack, @NotNull ItemDisplayContext displayContext, @NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int light, int overlay) {
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
    }
}