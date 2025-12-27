package mangopill.customized.integration.jei.category;

import mangopill.customized.common.recipe.CuttingBoardRecipe;
import mangopill.customized.common.registry.*;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.*;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.*;
import mezz.jei.api.gui.widgets.*;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.*;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.*;

import static mangopill.customized.common.util.CStringUtil.*;
import static mangopill.customized.common.util.RecipeUtil.*;
import static mangopill.customized.integration.jei.util.JeiUtil.*;

public class CuttingBoardRecipeCategory extends CRecipeCategory<CuttingBoardRecipe> {

    public CuttingBoardRecipeCategory(IGuiHelper helper) {
        super(CUTTING_BOARD, getCPngLoc("textures/gui/cutting_board"));
        title = CBlockRegistry.CUTTING_BOARD.get().getName();
        background = helper.createDrawable(image, 4, 4, 131, 53);
        icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(CItemRegistry.CUTTING_BOARD.get()));
        arrow = helper.drawableBuilder(image, 140, 0, 13, 14).buildAnimated(TICKS_PER_CYCLE, IDrawableAnimated.StartDirection.LEFT, false);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, CuttingBoardRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 31, 16).addIngredients(recipe.cuttingItem());
        builder.addSlot(RecipeIngredientRole.CATALYST, 49, 4).addIngredients(mergeIngredients(recipe.toolItem()));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 81, 36).addItemStacks(recipe.output());
        addProbabilityItemStackSlots(builder, recipe.probabilityOutput(), RecipeIngredientRole.OUTPUT,
                100, 17, SLOT_SIZE, 1, 0, 0, (b, i) -> b.setSlotName("probabilityOutput" + i));
    }

    @Override
    public void createRecipeExtras(IRecipeExtrasBuilder builder, CuttingBoardRecipe recipe, IFocusGroup focuses) {
        IRecipeSlotDrawablesView recipeSlots = builder.getRecipeSlots();
        List<IRecipeSlotDrawable> list = getIRecipeSlotDrawableByName(recipe.probabilityOutput(), recipeSlots, "probabilityOutput");
        IScrollGridWidget probabilityGrid = builder.addScrollGridWidget(list, 1, 1);
        probabilityGrid.setPosition(99, 16);
    }

    @Override
    public void getTooltip(ITooltipBuilder tooltip, CuttingBoardRecipe recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        List<Component> tooltipString = new ArrayList<>();
        addTooltipIfInArea(mouseX, mouseY, tooltipString, 81, 15, 13, 14, C_JEI_GUI.create("cutting_times", recipe.cuttingTimes()));
        tooltip.addAll(tooltipString);
    }

    @Override
    public void draw(CuttingBoardRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        arrow.draw(guiGraphics, 81, 15);
    }
}
