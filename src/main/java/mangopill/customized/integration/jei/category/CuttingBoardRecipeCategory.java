package mangopill.customized.integration.jei.category;

import mangopill.customized.Customized;
import mangopill.customized.common.recipe.CuttingBoardRecipe;
import mangopill.customized.common.registry.CBlockRegistry;
import mangopill.customized.common.registry.CItemRegistry;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.*;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.*;

import static mangopill.customized.common.util.RecipeUtil.*;
import static mangopill.customized.common.util.StringUtil.*;
import static mangopill.customized.integration.jei.util.JeiUtil.*;

public class CuttingBoardRecipeCategory extends CRecipeCategory<CuttingBoardRecipe> {

    public CuttingBoardRecipeCategory(IGuiHelper helper) {
        super(CUTTING_BOARD, getCPngLoc("textures/gui/cutting_board"));
        title = getComponent(CBlockRegistry.CUTTING_BOARD.get().getDescriptionId());
        background = helper.createDrawable(image, 4, 4, 117, 53);
        icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(CItemRegistry.CUTTING_BOARD.get()));
        arrow = helper.drawableBuilder(image, 126, 0, 13, 14).buildAnimated(TICKS_PER_CYCLE, IDrawableAnimated.StartDirection.LEFT, false);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, CuttingBoardRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 31, 16).addIngredients(recipe.cuttingItem());
        builder.addSlot(RecipeIngredientRole.CATALYST, 49, 4).addIngredients(mergeIngredients(recipe.toolItem()));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 81, 36).addItemStacks(recipe.output());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 100, 17).addItemStacks(recipe.probabilityOutput())
                .addRichTooltipCallback((s, t) -> t.add(getComponent("jei.gui." + Customized.MODID + ".probability",
                        String.format("%.2f", recipe.probability() * 100)).append("%").withStyle(ChatFormatting.AQUA)));
    }

    @Override
    public void getTooltip(ITooltipBuilder tooltip, CuttingBoardRecipe recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        List<Component> tooltipString = new ArrayList<>();
        addTooltipIfInArea(mouseX, mouseY, tooltipString, 81, 15, 13, 14, getComponent(
                "jei.gui." + Customized.MODID + ".cutting_times", recipe.cuttingTimes()));
        tooltip.addAll(tooltipString);
    }

    @Override
    public void draw(CuttingBoardRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        arrow.draw(guiGraphics, 81, 15);
    }
}
