package mangopill.customized.integration.jei.category;

import mangopill.customized.Customized;
import mangopill.customized.common.recipe.CuttingBoardRecipe;
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
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static mangopill.customized.common.util.ResourceUtil.*;
import static mangopill.customized.integration.jei.util.JeiUtil.*;

public class CuttingBoardRecipeCategory implements IRecipeCategory<CuttingBoardRecipe> {
    private final Component title;
    private final IDrawable background;
    private final IDrawable icon;

    public CuttingBoardRecipeCategory(IGuiHelper helper) {
        title = Component.translatable(CBlockRegistry.CUTTING_BOARD.get().getDescriptionId());
        background = helper.createDrawable(getCLoc("textures/gui/cutting_board" + ".png"),
                4, 4, 56, 83);
        icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(CItemRegistry.CUTTING_BOARD.get()));
    }

    @Override
    public RecipeType<CuttingBoardRecipe> getRecipeType() {
        return JeiUtil.CUTTING_BOARD;
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
    public void setRecipe(IRecipeLayoutBuilder builder, CuttingBoardRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 18, 9)
                .addIngredients(recipe.cuttingItem());
        List<ItemStack> toolStacks = recipe.toolItem().stream().flatMap(ingredient -> Arrays.stream(ingredient.getItems())).toList();
        builder.addSlot(RecipeIngredientRole.INPUT, 39, 34)
                .addItemStacks(toolStacks);
        builder.addSlot(RecipeIngredientRole.OUTPUT, 9, 66)
                .addItemStacks(recipe.output());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 27, 66)
                .addItemStacks(recipe.probabilityOutput())
                .addRichTooltipCallback((s, t) -> t.add(Component.translatable("jei.gui." + Customized.MODID + ".probability",
                        recipe.probability() * 100).append("%").withStyle(ChatFormatting.AQUA)));
    }

    @Override
    public void getTooltip(ITooltipBuilder tooltip, CuttingBoardRecipe recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        List<Component> tooltipString = new ArrayList<>();
        if (canAddTooltip(mouseX, mouseY, 18, 30, 14, 32)) {
            tooltipString.add(Component.translatable("jei.gui." + Customized.MODID + ".cutting_times",
                    recipe.cuttingTimes()));
        }
        tooltip.addAll(tooltipString);
    }
}
