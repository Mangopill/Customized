package mangopill.customized.integration.jei.category;

import mangopill.customized.common.recipe.CasseroleRecipe;
import mangopill.customized.common.registry.CBlockRegistry;
import mangopill.customized.common.registry.CItemRegistry;
import mangopill.customized.integration.jei.util.JeiUtil;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.*;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static mangopill.customized.common.util.ResourceUtil.*;
import static mangopill.customized.integration.jei.util.JeiUtil.*;

public class CasseroleRecipeCategory implements IRecipeCategory<CasseroleRecipe> {
    private final Component title;
    private final IDrawable background;
    private final IDrawable icon;

    public CasseroleRecipeCategory(IGuiHelper helper) {
        title = Component.translatable(CBlockRegistry.CASSEROLE.get().getDescriptionId());
        background = helper.createDrawable(getCLoc("textures/gui/casserole" + ".png"),
                4, 4, 108, 87);
        icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(CItemRegistry.CASSEROLE.get()));
    }

    @Override
    public RecipeType<CasseroleRecipe> getRecipeType() {
        return JeiUtil.CASSEROLE;
    }

    @Override
    public Component getTitle() {
        return title;
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    @Nullable
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, CasseroleRecipe recipe, IFocusGroup focuses) {
        setPotRecipe(builder, recipe, 18, 6, 6, 4,
                1, 58, 91, 70, 91, 45);
    }

    @Override
    public void getTooltip(ITooltipBuilder tooltip, CasseroleRecipe recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        List<Component> tooltipString = new ArrayList<>();
        addPotTooltip(recipe, mouseX, mouseY, tooltipString,
                26, 58, 56, 14,
                48, 69, 11, 11,
                95, 63, 8, 4);
        tooltip.addAll(tooltipString);
    }
}
