package mangopill.customized.integration.jei.category;

import com.mojang.blaze3d.vertex.PoseStack;
import mangopill.customized.Customized;
import mangopill.customized.common.recipe.*;
import mangopill.customized.common.registry.CItemRegistry;
import mangopill.customized.common.registry.CRecipeRegistry;
import mangopill.customized.common.util.value.PropertyValue;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.*;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.*;
import java.util.stream.*;

import static mangopill.customized.common.util.PropertyValueUtil.*;
import static mangopill.customized.common.util.StringUtil.*;
import static mangopill.customized.integration.jei.util.JeiUtil.*;

public class PropertyValueRecipeCategory extends CRecipeCategory<PropertyValueRecipeCategory.PropertyValueRecipeAdapter> {
    private static final int PAGE_ROWS = 5;
    private static final int PAGE_COLS = 3;
    public static final int PAGE_TOTAL = PAGE_ROWS * PAGE_COLS;

    public PropertyValueRecipeCategory(IGuiHelper helper) {
        super(PROPERTY_VALUE, getCPngLoc("textures/gui/property_value"));
        title = getComponent("jei.category." + Customized.MODID + ".property_value");
        background = helper.createDrawable(image, 4, 4, 180, 92);
        icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(CItemRegistry.CHEF_HAT.get()));
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, PropertyValueRecipeAdapter recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.CATALYST, 2, 2).addIngredients(recipe.ingredient);
        List<List<NutrientCategoryRecipe>> nutrientList = getNutrientCategorySubList(recipe);
        int spacing = 48;
        for (int i = 0; i < Math.min(nutrientList.size(), PAGE_COLS); i++) {
            addNutrientSlots(builder, nutrientList.get(i), RecipeIngredientRole.OUTPUT, 36 + (i * spacing), 2, SLOT_SIZE, 5, 1, 0, null);
        }
    }

    @Override
    public void draw(PropertyValueRecipeAdapter recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        Minecraft mc = Minecraft.getInstance();
        int spacing = 48;
        List<List<NutrientCategoryRecipe>> nutrientList = getNutrientCategorySubList(recipe);
        for (int i = 0; i < nutrientList.size(); i++) {
            List<NutrientCategoryRecipe> categoryRecipes = nutrientList.get(i);
            for (int j = 0; j < categoryRecipes.size(); j++) {
                NutrientCategoryRecipe categoryRecipe = categoryRecipes.get(j);
                Component component = getComponent(Stream.of(recipe.propertyValue.getValue()).map(map -> map.get(categoryRecipe.name())).toList().getFirst().toString()).append("%")
                        .withStyle(ChatFormatting.ITALIC)
                        .withStyle(ChatFormatting.UNDERLINE);
                PoseStack poseStack = guiGraphics.pose();
                poseStack.pushPose();
                int textWidth = mc.font.width(component);
                float scale = Math.min(1.0f, (float) (spacing - SLOT_SIZE) / (float) textWidth);
                int x = 54 + (i * spacing);
                int y = 8 + (SLOT_SIZE * j);
                float scaledWidth = textWidth * scale;
                float xOffset = ((spacing - SLOT_SIZE) - scaledWidth) / 2;
                poseStack.translate(x + xOffset, y, 0);
                poseStack.scale(scale, scale, 1.0F);
                guiGraphics.drawString(mc.font, component, 0, 0, categoryRecipe.getColorWithAlpha(), false);
                poseStack.popPose();
            }
        }
    }

    public static List<List<NutrientCategoryRecipe>> getNutrientCategorySubList(PropertyValueRecipeAdapter recipe) {
        List<NutrientCategoryRecipe> allNutrients = recipe.propertyValue.getValue().entrySet().stream().filter(group -> Minecraft.getInstance().level != null)
                .map(entry -> getNutrientCategoryByName(Minecraft.getInstance().level, entry.getKey()).getFirst()).toList();
        return partitionList(allNutrients, PAGE_ROWS).toList();
    }

    public record PropertyValueRecipeAdapter(Ingredient ingredient, PropertyValue propertyValue) {
        public static List<PropertyValueRecipeAdapter> getPropertyValueList() {
            List<PropertyValueRecipe> oldList = getRecipeList(CRecipeRegistry.PROPERTY_VALUE.get());
            List<Ingredient> allIngredients = oldList.stream().map(PropertyValueRecipe::getIngredients).flatMap(NonNullList::stream).toList();
            return allIngredients.stream().filter(current -> allIngredients.stream().noneMatch(other ->
                            !other.equals(current) && Arrays.stream(current.getItems()).allMatch(other))).flatMap(ingredient -> {
                                PropertyValue propertyValue = getPropertyValue(ingredient.getItems()[0], oldList);
                                List<Map.Entry<String, Float>> entries = new ArrayList<>(propertyValue.getValue().entrySet());
                                return partitionList(entries, PAGE_TOTAL).map(subEntries -> {
                                            Map<String, Float> subMap = subEntries.stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
                                            PropertyValue subPropertyValue = new PropertyValue(subMap);
                                            return new PropertyValueRecipeAdapter(ingredient, subPropertyValue);
                                        });
                            }).toList();
        }
    }

    public static <T> Stream<List<T>> partitionList(List<T> list, int pageSize) {
        return IntStream.range(0, (list.size() + pageSize - 1) / pageSize).mapToObj(i -> list.subList(i * pageSize, Math.min((i + 1) * pageSize, list.size())));
    }
}