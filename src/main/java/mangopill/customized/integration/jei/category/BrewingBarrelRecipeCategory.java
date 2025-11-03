package mangopill.customized.integration.jei.category;

import mangopill.customized.Customized;
import mangopill.customized.common.recipe.BrewingBarrelRecipe;
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

public class BrewingBarrelRecipeCategory implements IRecipeCategory<BrewingBarrelRecipe> {
    private final Component title;
    private final IDrawable background;
    private final IDrawable icon;

    public BrewingBarrelRecipeCategory(IGuiHelper helper) {
        title = Component.translatable(CBlockRegistry.BREWING_BARREL.get().getDescriptionId());
        background = helper.createDrawable(getCLoc("textures/gui/brewing_barrel" + ".png"),
                4, 4, 54, 61);
        icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(CItemRegistry.BREWING_BARREL.get()));
    }

    @Override
    public RecipeType<BrewingBarrelRecipe> getRecipeType() {
        return JeiUtil.BREWING_BARREL;
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
    public void setRecipe(IRecipeLayoutBuilder builder, BrewingBarrelRecipe recipe, IFocusGroup focuses) {
        int slotSize = 18;
        for (int row = 0; row < 2; ++row) {
            for (int col = 0; col < 2; ++col) {
                int index = row * 2 + col;
                if (index < recipe.getIngredientItem().size()) {
                    builder.addSlot(RecipeIngredientRole.INPUT, col * slotSize + 1, row * slotSize + 1)
                            .addIngredients(recipe.getIngredientItem().get(index));
                }
            }
        }
        builder.addSlot(RecipeIngredientRole.INPUT, 37, 44)
                .addIngredients(recipe.getContainerItem());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 37, 19)
                .addItemStack(recipe.getOutput());
    }

    @Override
    public void getTooltip(ITooltipBuilder tooltip, BrewingBarrelRecipe recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        List<Component> tooltipString = new ArrayList<>();
        if (canAddTooltip(mouseX, mouseY, 38, 3, 11, 13)) {
            tooltipString.add(Component.translatable("jei.gui." + Customized.MODID + ".cook_time",
                    recipe.getCookingTime() * 13 / 20));
        }
        if (canAddTooltip(mouseX, mouseY, 41, 37, 8, 4)) {
            tooltipString.add(Component.translatable("jei.gui." + Customized.MODID + ".container"));
        }
        tooltip.addAll(tooltipString);
    }
}
