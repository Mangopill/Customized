package mangopill.customized.client.util;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import mangopill.customized.common.block.entity.AbstractPotBlockEntity;
import mangopill.customized.common.recipe.PropertyValueRecipe;
import mangopill.customized.common.util.category.NutrientCategory;
import mangopill.customized.common.util.value.PropertyValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public final class ClientUtil {
    private ClientUtil() {
    }
    public static int getMaxValueColor(Level level, List<ItemStack> stackList) {
        Map<NutrientCategory, Float> nutrientSums = new HashMap<>();
        for (ItemStack stack : stackList) {
            @NotNull PropertyValue propertyValue = PropertyValueRecipe.getPropertyValue(stack, level);
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
                    if (potBlockEntity.getLevel() != null) {
                        ItemStack renderItemStack = newStack.copy();
                        renderItemStack.setCount(1);
                        Minecraft.getInstance().getItemRenderer().renderStatic(renderItemStack, ItemDisplayContext.FIXED, light, overlay, poseStack, buffer, null, seed);
                    }
                    poseStack.popPose();
                }
            }
        }
    }
}
