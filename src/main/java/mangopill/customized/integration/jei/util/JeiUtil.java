package mangopill.customized.integration.jei.util;

import mangopill.customized.Customized;
import mangopill.customized.common.recipe.*;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;

import java.util.List;
import java.util.function.Supplier;

public final class JeiUtil {
    private JeiUtil() {
    }
    public static final RecipeType<CasseroleRecipe> CASSEROLE = RecipeType.create(Customized.MODID, "casserole", CasseroleRecipe.class);
    public static final RecipeType<BrewingBarrelRecipe> BREWING_BARREL = RecipeType.create(Customized.MODID, "brewing_barrel", BrewingBarrelRecipe.class);
    public static final RecipeType<CrateRecipe> CRATE = RecipeType.create(Customized.MODID, "crate", CrateRecipe.class);
    public static final RecipeType<CuttingBoardRecipe> CUTTING_BOARD = RecipeType.create(Customized.MODID, "cutting_board", CuttingBoardRecipe.class);

    public static<I extends RecipeInput, T extends Recipe<I>>  List<T> getRecipeList(net.minecraft.world.item.crafting.RecipeType<T> recipeType) {
        return Minecraft.getInstance().level.getRecipeManager()
                .getAllRecipesFor(recipeType)
                .stream()
                .map(RecipeHolder::value)
                .toList();
    }

    public static boolean canAddTooltip(double mouseX, double mouseY, double xStart, double yStart, double width, double height) {
        return mouseX >= xStart && mouseX <= xStart + width && mouseY >= yStart && mouseY <= yStart + height;
    }

    public static void setPotRecipe(IRecipeLayoutBuilder builder, CasseroleRecipe recipe,
                                    int slotSize, int slotCount1, int slotCount2, int lineSpacing,
                                    int spiceXStart, int spiceYStart, int containerXStart,
                                    int containerYStart, int outputXStart, int outputYStart) {
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
        builder.addSlot(RecipeIngredientRole.INPUT, containerXStart, containerYStart)
                .addIngredients(recipe.getContainerItem());
        builder.addSlot(RecipeIngredientRole.OUTPUT, outputXStart, outputYStart)
                .addItemStack(recipe.getOutput());
    }

    public static void addPotTooltip(CasseroleRecipe recipe, double mouseX, double mouseY, List<Component> tooltipString,
                                     int timeXStart, int timeYStart, int timeWidth, int timeHeight,
                                     int fireXStart, int fireYStart, int fireWidth, int fireHeight,
                                     int containerXStart, int containerYStart, int containerWidth, int containerHeight) {
        if (canAddTooltip(mouseX, mouseY, timeXStart, timeYStart, timeWidth, timeHeight)) {
            tooltipString.add(Component.translatable("jei.gui." + Customized.MODID + ".cook_time",
                    recipe.getCookingTime() / 20));
        }
        if (canAddTooltip(mouseX, mouseY, fireXStart, fireYStart, fireWidth, fireHeight)) {
            tooltipString.add(Component.translatable("jei.gui." + Customized.MODID + ".fire"));
        }
        if (canAddTooltip(mouseX, mouseY, containerXStart, containerYStart, containerWidth, containerHeight)) {
            tooltipString.add(Component.translatable("jei.gui." + Customized.MODID + ".container"));
        }
    }

    public static void addJeiInfo(IRecipeRegistration registration, Supplier<Item> item) {
        registration.addIngredientInfo(new ItemStack(item.get()), VanillaTypes.ITEM_STACK,
                Component.translatable("jei.info." + Customized.MODID + "." + BuiltInRegistries.ITEM.getKey(item.get()).getPath()));
    }
}
