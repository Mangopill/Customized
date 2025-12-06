package mangopill.customized.integration.jei.category;

import mangopill.customized.Customized;
import mangopill.customized.common.recipe.CrateRecipe;
import mangopill.customized.common.registry.CBlockRegistry;
import mangopill.customized.common.registry.CItemRegistry;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.drawable.*;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.*;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.*;

import static mangopill.customized.common.util.StringUtil.*;
import static mangopill.customized.integration.jei.util.JeiUtil.*;

public class CrateRecipeCategory extends CRecipeCategory<CrateRecipe> {

    public CrateRecipeCategory(IGuiHelper helper) {
        super(CRATE, getCPngLoc("textures/gui/crate"));
        title = getComponent(CBlockRegistry.CRATE.get().getDescriptionId());
        background = helper.createDrawable(image, 4, 4, 77, 65);
        icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(CItemRegistry.CRATE.get()));
        drive = helper.createDrawable(image, 85, 0, 16, 16);
        arrow = helper.drawableBuilder(image, 85, 16, 15, 22).buildAnimated(TICKS_PER_CYCLE, IDrawableAnimated.StartDirection.TOP, false);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, CrateRecipe recipe, IFocusGroup focuses) {
        List<ItemStack> itemStacks = Arrays.stream(recipe.getIngredientItem().getItems()).map(ItemStack::copy).peek(itemStack -> itemStack.setCount(recipe.getIngredientCount())).toList();
        builder.addSlot(RecipeIngredientRole.INPUT, 5, 30).addItemStacks(itemStacks);
        builder.addSlot(RecipeIngredientRole.OUTPUT, 59, 47).addItemStack(recipe.getOutput());
    }

    @Override
    public void getTooltip(ITooltipBuilder tooltip, CrateRecipe recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        List<Component> tooltipString = new ArrayList<>();
        addTooltipIfInArea(mouseX, mouseY, tooltipString, 59, 22, 15, 22, getComponent("jei.gui." + Customized.MODID + ".cook_time", recipe.getCookingTime() / 20));
        if (recipe.isSunny()) {
            addTooltipIfInArea(mouseX, mouseY, tooltipString, 59, 0, 16, 16, getComponent("jei.gui." + Customized.MODID + ".sunny"));
        }
        tooltip.addAll(tooltipString);
    }

    @Override
    public void draw(CrateRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        if (recipe.isSunny()) {
            drive.draw(guiGraphics, 59, 0);
        }
        arrow.draw(guiGraphics, 59, 22);
    }
}
