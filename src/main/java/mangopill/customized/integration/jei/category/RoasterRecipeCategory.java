package mangopill.customized.integration.jei.category;

import mangopill.customized.common.block.record.PotRecord;
import mangopill.customized.common.recipe.RoasterRecipe;
import mangopill.customized.common.registry.*;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.*;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.*;

import static mangopill.customized.common.util.CStringUtil.*;
import static mangopill.customized.integration.jei.util.JeiUtil.*;

public class RoasterRecipeCategory extends CRecipeCategory<RoasterRecipe> {

    public RoasterRecipeCategory(IGuiHelper helper) {
        super(ROASTER, getCPngLoc("textures/gui/roaster"));
        title = CBlockRegistry.ROASTER.get().getName();
        background = helper.createDrawable(image, 4, 4, 108, 121);
        icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(CItemRegistry.ROASTER.get()));
        drive = helper.createDrawable(image, 117, 14, 11, 11);
        arrow = helper.drawableBuilder(image, 117, 0, 56, 14).buildAnimated(TICKS_PER_CYCLE, IDrawableAnimated.StartDirection.LEFT, false);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RoasterRecipe recipe, IFocusGroup focuses) {
        setPotRecipe(builder, recipe, SLOT_SIZE, PotRecord.ROASTER.ingredientCount(), PotRecord.ROASTER.seasoningCount(), PotRecord.ROASTER.spiceCount(), 4,
                10, 1, 1, 80, 91, 92, 91, 67);
    }

    @Override
    public void getTooltip(ITooltipBuilder tooltip, RoasterRecipe recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        List<Component> tooltipString = new ArrayList<>();
        addPotTooltip(recipe, mouseX, mouseY, tooltipString, 27, 100, 56, 14, 49,
                110, 11, 11, 95, 85, 8, 4);
        tooltip.addAll(tooltipString);
    }

    @Override
    public void draw(RoasterRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        if (recipe.isHeated()) {
            drive.draw(guiGraphics, 49, 110);
        }
        arrow.draw(guiGraphics, 27, 100);
    }
}
