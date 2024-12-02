package mangopill.customized.integration.jei.util;

import mangopill.customized.Customized;
import mangopill.customized.common.recipe.CasseroleRecipe;
import mangopill.customized.common.registry.ModItemRegistry;
import mangopill.customized.common.registry.ModRecipeRegistry;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class JeiUtil {
    private JeiUtil() {
    }
    public static final RecipeType<CasseroleRecipe> CASSEROLE = RecipeType.create(Customized.MODID, "casserole", CasseroleRecipe.class);

    public static List<CasseroleRecipe> getCasseroleRecipeList() {
        if (Minecraft.getInstance().level != null) {
            return Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(ModRecipeRegistry.CASSEROLE.get()).stream().map(RecipeHolder::value).toList();
        } else {
            return new ArrayList<>();
        }
    }
    public static boolean canAddTooltip(double mouseX, double mouseY, double xStart, double yStart, double width, double height) {
        return mouseX >= xStart && mouseX <= xStart + width && mouseY >= yStart && mouseY <= yStart + height;
    }

    public static void setPotRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull CasseroleRecipe recipe,
                                    int slotSize, int slotCount1, int slotCount2, int lineSpacing,
                                    int spiceXStart, int spiceYStart, int outputXStart, int outputYStart) {
        for (int col = 0; col < slotCount1; ++col) {
            if (col < recipe.getIngredientItem().size()) {
                builder.addSlot(RecipeIngredientRole.INPUT, col * slotSize + 1, 1)
                        .addIngredients(recipe.getIngredientItem().get(col));
            }
        }
        for (int col = 0; col < slotCount2; ++col) {
            if (col < recipe.getSeasoningItem().size()) {
                builder.addSlot(RecipeIngredientRole.INPUT, col * slotSize + 1, slotSize + lineSpacing + 1)
                        .addIngredients(recipe.getSeasoningItem().get(col));
            }
        }
        builder.addSlot(RecipeIngredientRole.INPUT, spiceXStart, spiceYStart)
                .addIngredients(recipe.getSpiceItem());
        builder.addSlot(RecipeIngredientRole.OUTPUT, outputXStart, outputYStart)
                .addItemStack(recipe.getOutput());
    }

    public static void addPotTooltip(@NotNull CasseroleRecipe recipe, double mouseX, double mouseY, List<Component> tooltipString,
                                     int timeXStart, int timeYStart, int timeWidth, int timeHeight,
                                     int fireXStart, int fireYStart, int fireWidth, int fireHeight,
                                     int containerXStart, int containerYStart, int containerWidth, int containerHeight) {
        if (canAddTooltip(mouseX, mouseY, timeXStart, timeYStart, timeWidth, timeHeight)) {
            tooltipString.add(Component.translatable("gui.jei." + Customized.MODID + ".cook_time",
                    recipe.getCookingTime() / 20));
        }
        if (canAddTooltip(mouseX, mouseY, fireXStart, fireYStart, fireWidth, fireHeight)) {
            tooltipString.add(Component.translatable("gui.jei." + Customized.MODID + ".fire"));
        }
        if (canAddTooltip(mouseX, mouseY, containerXStart, containerYStart, containerWidth, containerHeight)) {
            tooltipString.add(Component.translatable("gui.jei." + Customized.MODID + ".container",
                    Component.translatable(ModItemRegistry.FAMOUS_DISH_PLATE.get().getDescriptionId())));
        }
    }
}
