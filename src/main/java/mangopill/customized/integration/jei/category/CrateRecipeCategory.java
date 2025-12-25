package mangopill.customized.integration.jei.category;

import mangopill.customized.common.recipe.CrateRecipe;
import mangopill.customized.common.registry.*;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.*;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.*;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.*;

import static mangopill.customized.common.util.CStringUtil.*;
import static mangopill.customized.common.util.RecipeUtil.*;
import static mangopill.customized.integration.jei.util.JeiUtil.*;

public class CrateRecipeCategory extends CRecipeCategory<CrateRecipe> {

    public CrateRecipeCategory(IGuiHelper helper) {
        super(CRATE, getCPngLoc("textures/gui/crate"));
        title = CBlockRegistry.CRATE.get().getName();
        background = helper.createDrawable(image, 4, 4, 77, 65);
        icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(CItemRegistry.CRATE.get()));
        drive = helper.createDrawable(image, 85, 0, 16, 16);
        arrow = helper.drawableBuilder(image, 85, 16, 15, 22).buildAnimated(TICKS_PER_CYCLE, IDrawableAnimated.StartDirection.TOP, false);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, CrateRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 5, 30).addItemStacks(toStackList(List.of(recipe.ingredientItem()), stack -> stack.setCount(recipe.ingredientCount())));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 59, 47).addItemStack(recipe.output());
    }

    @Override
    public void getTooltip(ITooltipBuilder tooltip, CrateRecipe recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        List<Component> tooltipString = new ArrayList<>();
        addTooltipIfInArea(mouseX, mouseY, tooltipString, 59, 22, 15, 22, C_JEI_GUI.create("cook_time", recipe.cookingTime() / 20));
        if (recipe.sunny()) {
            addTooltipIfInArea(mouseX, mouseY, tooltipString, 59, 0, 16, 16, C_JEI_GUI.create("sunny"));
        }
        tooltip.addAll(tooltipString);
    }

    @Override
    public void draw(CrateRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        if (recipe.sunny()) {
            drive.draw(guiGraphics, 59, 0);
        }
        arrow.draw(guiGraphics, 59, 22);
    }
}
