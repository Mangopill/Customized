package mangopill.customized.integration.jei.category;

import mangopill.customized.Customized;
import mangopill.customized.common.recipe.CasseroleRecipe;
import mangopill.customized.common.registry.ModBlockRegistry;
import mangopill.customized.common.registry.ModItemRegistry;
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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static mangopill.customized.integration.jei.util.JeiUtil.*;

public class CasseroleRecipeCategory implements IRecipeCategory<CasseroleRecipe> {
    private final Component title;
    private final IDrawable background;
    private final IDrawable icon;

    public CasseroleRecipeCategory(IGuiHelper helper) {
        title = Component.translatable(ModBlockRegistry.CASSEROLE.get().getDescriptionId());
        background = helper.createDrawable(ResourceLocation.fromNamespaceAndPath(Customized.MODID, "textures/gui/casserole" + ".png"),
                4, 4, 108, 67);
        icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModItemRegistry.CASSEROLE.get()));
    }

    @Override
    public @NotNull RecipeType<CasseroleRecipe> getRecipeType() {
        return JeiUtil.CASSEROLE;
    }

    @Override
    public @NotNull Component getTitle() {
        return title;
    }

    @Override
    public @NotNull IDrawable getBackground() {
        return background;
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull CasseroleRecipe recipe, @NotNull IFocusGroup focuses) {
        setPotRecipe(builder, recipe, 18, 6, 6, 4,
                1, 45, 91, 45);
    }

    @Override
    public void getTooltip(@NotNull ITooltipBuilder tooltip, @NotNull CasseroleRecipe recipe, @NotNull IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        List<Component> tooltipString = new ArrayList<>();
        addPotTooltip(recipe, mouseX, mouseY, tooltipString,
                26, 45, 56, 14,
                48, 56, 11, 11,
                95, 63, 8, 4);
        tooltip.addAll(tooltipString);
    }
}
