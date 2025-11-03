package mangopill.customized.integration.jei.category;

import mangopill.customized.Customized;
import mangopill.customized.common.recipe.CrateRecipe;
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

public class CrateRecipeCategory implements IRecipeCategory<CrateRecipe> {
    private final Component title;
    private final IDrawable background;
    private final IDrawable icon;

    public CrateRecipeCategory(IGuiHelper helper) {
        title = Component.translatable(CBlockRegistry.CRATE.get().getDescriptionId());
        background = helper.createDrawable(getCLoc("textures/gui/crate" + ".png"),
                4, 4, 162, 71);
        icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(CItemRegistry.CRATE.get()));
    }

    @Override
    public RecipeType<CrateRecipe> getRecipeType() {
        return JeiUtil.CRATE;
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
    public void setRecipe(IRecipeLayoutBuilder builder, CrateRecipe recipe, IFocusGroup focuses) {
        int slotSize = 18;
        for (int col = 0; col < 9; ++col) {
            if (col < recipe.getIngredientItem().size()) {
                builder.addSlot(RecipeIngredientRole.INPUT, col * slotSize + 1, slotSize)
                        .addIngredients(recipe.getIngredientItem().get(col));
            }
        }
        builder.addSlot(RecipeIngredientRole.OUTPUT, 73, 54)
                .addItemStack(recipe.getOutput());
    }

    @Override
    public void getTooltip(ITooltipBuilder tooltip, CrateRecipe recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        List<Component> tooltipString = new ArrayList<>();
        if (canAddTooltip(mouseX, mouseY, 74, 36, 14, 15)) {
            tooltipString.add(Component.translatable("jei.gui." + Customized.MODID + ".cook_time",
                    recipe.getCookingTime() * 13 / 20));
        }
        if (canAddTooltip(mouseX, mouseY, 73, 0, 16, 16)) {
            tooltipString.add(Component.translatable("jei.gui." + Customized.MODID + ".sunny"));
        }
        tooltip.addAll(tooltipString);
    }
}
