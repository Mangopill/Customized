package mangopill.customized.integration.jei.category;

import mangopill.customized.Customized;
import mangopill.customized.common.recipe.BrewingBarrelRecipe;
import mangopill.customized.common.registry.CBlockRegistry;
import mangopill.customized.common.registry.CItemRegistry;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.*;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

import static mangopill.customized.common.util.StringUtil.*;
import static mangopill.customized.integration.jei.util.JeiUtil.*;

public class BrewingBarrelRecipeCategory extends CRecipeCategory<BrewingBarrelRecipe> {

    public BrewingBarrelRecipeCategory(IGuiHelper helper) {
        super(BREWING_BARREL, getCPngLoc("textures/gui/brewing_barrel"));
        title = getComponent(CBlockRegistry.BREWING_BARREL.get().getDescriptionId());
        background = helper.createDrawable(image, 4, 4, 95, 82);
        icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(CItemRegistry.BREWING_BARREL.get()));
        arrow = helper.drawableBuilder(image, 105, 0, 17, 17).buildAnimated(TICKS_PER_CYCLE, IDrawableAnimated.StartDirection.LEFT, false);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, BrewingBarrelRecipe recipe, IFocusGroup focuses) {
        addIngredientSlots(builder, recipe.getIngredientItem(), RecipeIngredientRole.INPUT,
                2, 24, SLOT_SIZE, 2, 2, 0, null);
        builder.addSlot(RecipeIngredientRole.INPUT, 67, 65).addIngredients(recipe.getContainerItem());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 42, 65).addItemStack(recipe.getOutput());
    }

    @Override
    public void getTooltip(ITooltipBuilder tooltip, BrewingBarrelRecipe recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        List<Component> tooltipString = new ArrayList<>();
        addTooltipIfInArea(mouseX, mouseY, tooltipString, 21, 63, 17, 17, getComponent("jei.gui." + Customized.MODID + ".cook_time", recipe.getCookingTime() * 13 / 20));
        addTooltipIfInArea(mouseX, mouseY, tooltipString, 60, 69, 4, 8, getComponent("jei.gui." + Customized.MODID + ".container"));
        tooltip.addAll(tooltipString);
    }

    @Override
    public void draw(BrewingBarrelRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        arrow.draw(guiGraphics, 21, 63);
    }
}
