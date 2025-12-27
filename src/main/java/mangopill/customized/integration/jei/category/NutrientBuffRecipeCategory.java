package mangopill.customized.integration.jei.category;

import com.mojang.datafixers.util.Pair;
import mangopill.customized.common.recipe.*;
import mangopill.customized.common.registry.CItemRegistry;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.*;
import mezz.jei.api.gui.ingredient.*;
import mezz.jei.api.gui.widgets.*;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.ItemStack;

import java.util.*;

import static mangopill.customized.common.util.CStringUtil.*;
import static mangopill.customized.common.util.PropertyValueUtil.*;
import static mangopill.customized.integration.jei.util.JeiUtil.*;

public class NutrientBuffRecipeCategory extends CRecipeCategory<NutrientBuffRecipe> {

    public NutrientBuffRecipeCategory(IGuiHelper helper) {
        super(NUTRIENT_BUFF, getCPngLoc("textures/gui/nutrient_buff"));
        title = C_JEI_CATEGORY.create("nutrient_buff");
        background = helper.createDrawable(image, 4, 4, 105, 92);
        icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(CItemRegistry.CHEF_HAT.get()));
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, NutrientBuffRecipe recipe, IFocusGroup focuses) {
        List<List<Pair<NutrientCategoryRecipe, Float>>> recipesLists = recipe.nutrientCategory().stream()
                .filter(set -> Minecraft.getInstance().level != null).map(set -> set.stream()
                        .flatMap(pair -> getNutrientCategoryByName(Minecraft.getInstance().level, pair.getFirst())
                                .stream().map(recipeHolder -> Pair.of(recipeHolder, pair.getSecond()))).toList()).toList();
        addNutrientListSlots(builder, recipesLists.stream().map(list -> list.stream().map(Pair::getFirst).toList()).toList(), RecipeIngredientRole.INPUT,
                1, 56, SLOT_SIZE, 0, 5, 0, (b, i) ->
                        b.addRichTooltipCallback((s, tB) -> recipesLists.get(i).forEach(pair ->
                                tB.add(getTooltipPropertyComponent(pair.getFirst().name(), pair.getSecond(), pair.getFirst().getColorWithAlpha())))));
        addIngredientSlots(builder, recipe.pot(), RecipeIngredientRole.CATALYST,
                73, 15, SLOT_SIZE, 0, 2, 0, null);
    }

    @Override
    public void createRecipeExtras(IRecipeExtrasBuilder builder, NutrientBuffRecipe recipe, IFocusGroup focuses) {
        IRecipeSlotDrawablesView recipeSlots = builder.getRecipeSlots();
        IScrollGridWidget nutrientGrid = builder.addScrollGridWidget(recipeSlots.getSlots(RecipeIngredientRole.INPUT), 5, 2);
        nutrientGrid.setPosition(0, 55);
        IScrollGridWidget potGrid = builder.addScrollGridWidget(recipeSlots.getSlots(RecipeIngredientRole.CATALYST), 1, 2);
        potGrid.setPosition(72, 14);
    }

    @Override
    public void getTooltip(ITooltipBuilder tooltip, NutrientBuffRecipe recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        List<Component> tooltipString = new ArrayList<>();
        addTooltipIfInArea(mouseX, mouseY, tooltipString, 96, 0, 9, 9, C_JEI_GUI.create("nutrient_buff_tooltip"));
        addTooltipIfInArea(mouseX, mouseY, tooltipString, 21, 10, 30, 30, recipe.effect().value().getDisplayName(), C_JEI_GUI.create("nutrient_buff", recipe.duration(), recipe.probability(), recipe.shrinkNutrition(), recipe.shrinkSaturation()));
        tooltip.addAll(tooltipString);
    }

    @Override
    public void draw(NutrientBuffRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        Holder<MobEffect> effect = recipe.effect();
        TextureAtlasSprite sprite = Minecraft.getInstance().getMobEffectTextures().get(effect);
        guiGraphics.blit(21, 10, 0, 30, 30, sprite);
    }
}
