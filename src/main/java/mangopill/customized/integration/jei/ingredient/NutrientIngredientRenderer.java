package mangopill.customized.integration.jei.ingredient;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import mangopill.customized.Customized;
import mangopill.customized.common.recipe.NutrientCategoryRecipe;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.TooltipFlag;

import java.util.*;

import static mangopill.customized.common.util.StringUtil.*;

public class NutrientIngredientRenderer implements IIngredientRenderer<NutrientCategoryRecipe> {

    @Override
    public void render(GuiGraphics guiGraphics, NutrientCategoryRecipe recipe) {
        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();
        int textureSize = 20;
        float scale = (float) getWidth() / (float) textureSize;
        poseStack.scale(scale, scale, 1.0F);
        RenderSystem.setShaderTexture(0, recipe.icon());
        guiGraphics.blit(recipe.icon(), 0, 0, 0, 0, textureSize, textureSize, textureSize, textureSize);
        poseStack.popPose();
    }

    @Override
    public List<Component> getTooltip(NutrientCategoryRecipe ingredient, TooltipFlag tooltipFlag) {
        return new ArrayList<>();
    }

    @Override
    public void getTooltip(ITooltipBuilder tooltip, NutrientCategoryRecipe recipe, TooltipFlag tooltipFlag) {
        tooltip.add(getComponent("property." + Customized.MODID + ".nutrient_category." + recipe.name()).withStyle(ChatFormatting.BOLD).withColor(recipe.getColorWithAlpha()));
        tooltip.add(getComponent("jei.gui." + Customized.MODID + ".nutrition", recipe.nutrition()).withStyle(ChatFormatting.GOLD));
        tooltip.add(getComponent("jei.gui." + Customized.MODID + ".saturation", recipe.saturation()).withStyle(ChatFormatting.GOLD));
    }
}