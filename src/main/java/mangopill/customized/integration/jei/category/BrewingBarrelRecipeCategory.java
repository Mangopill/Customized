package mangopill.customized.integration.jei.category;

import mangopill.customized.Customized;
import mangopill.customized.common.recipe.BrewingBarrelRecipe;
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

public class BrewingBarrelRecipeCategory implements IRecipeCategory<BrewingBarrelRecipe> {
    private final Component title;
    private final IDrawable background;
    private final IDrawable icon;

    public BrewingBarrelRecipeCategory(IGuiHelper helper) {
        title = Component.translatable(ModBlockRegistry.BREWING_BARREL.get().getDescriptionId());
        background = helper.createDrawable(ResourceLocation.fromNamespaceAndPath(Customized.MODID, "textures/gui/brewing_barrel" + ".png"),
                4, 4, 54, 41);
        icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModItemRegistry.BREWING_BARREL.get()));
    }

    @Override
    public @NotNull RecipeType<BrewingBarrelRecipe> getRecipeType() {
        return JeiUtil.BREWING_BARREL;
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
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull BrewingBarrelRecipe recipe, @NotNull IFocusGroup focuses) {
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
        builder.addSlot(RecipeIngredientRole.OUTPUT, 37, 19)
                .addItemStack(recipe.getOutput());
    }

    @Override
    public void getTooltip(@NotNull ITooltipBuilder tooltip, @NotNull BrewingBarrelRecipe recipe, @NotNull IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        List<Component> tooltipString = new ArrayList<>();
        if (canAddTooltip(mouseX, mouseY, 38, 3, 11, 13)) {
            tooltipString.add(Component.translatable("gui.jei." + Customized.MODID + ".cook_time",
                    recipe.getCookingTime() / 20));
        }
        if (canAddTooltip(mouseX, mouseY, 41, 37, 8, 4)) {
            tooltipString.add(Component.translatable("gui.jei." + Customized.MODID + ".container",
                    Component.translatable(recipe.getContainerItem().getItems()[0].getDescriptionId())));
        }
        tooltip.addAll(tooltipString);
    }
}
