package mangopill.customized.integration.jei.category;

import mangopill.customized.common.block.record.PotRecord;
import mangopill.customized.common.recipe.CasseroleRecipe;
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

public class CasseroleRecipeCategory extends CRecipeCategory<CasseroleRecipe> {

    public CasseroleRecipeCategory(IGuiHelper helper) {
        super(CASSEROLE, getCPngLoc("textures/gui/casserole"));
        title = CBlockRegistry.CASSEROLE.get().getName();
        background = helper.createDrawable(image, 4, 4, 108, 99);
        icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(CItemRegistry.CASSEROLE.get()));
        fluidOverlay = helper.createDrawable(image, 117, 0, 12, 12);
        drive = helper.createDrawable(image, 117, 26, 11, 11);
        arrow = helper.drawableBuilder(image, 117, 12, 56, 14).buildAnimated(TICKS_PER_CYCLE, IDrawableAnimated.StartDirection.LEFT, false);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, CasseroleRecipe recipe, IFocusGroup focuses) {
        setPotRecipe(builder, recipe, SLOT_SIZE, PotRecord.CASSEROLE.ingredientCount(), PotRecord.CASSEROLE.seasoningCount(), PotRecord.CASSEROLE.spiceCount(), 4,
                1, 1, 1, 58, 91, 70, 91, 45);
        setFluidRecipe(builder, recipe, 49, 51, 10, 10, fluidOverlay, -1, -1);
    }

    @Override
    public void getTooltip(ITooltipBuilder tooltip, CasseroleRecipe recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        List<Component> tooltipString = new ArrayList<>();
        addPotTooltip(recipe, mouseX, mouseY, tooltipString, 27, 78, 56, 14, 49,
                88, 11, 11, 95, 63, 8, 4);
        tooltip.addAll(tooltipString);
    }

    @Override
    public void draw(CasseroleRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        if (recipe.isHeated()) {
            drive.draw(guiGraphics, 49, 88);
        }
        arrow.draw(guiGraphics, 27, 78);
    }
}
