package mangopill.customized.integration.jei.util;

import mangopill.customized.Customized;
import mangopill.customized.common.item.crafting.ProbabilityItemStack;
import mangopill.customized.common.recipe.*;
import mangopill.customized.common.util.CStringUtil;
import mangopill.customized.integration.jei.category.PropertyValueRecipeCategory;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.*;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.recipe.*;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.*;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.neoforged.neoforge.fluids.*;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.*;

import static mangopill.customized.common.util.CStringUtil.*;
import static mangopill.customized.common.util.RecipeUtil.*;

public final class JeiUtil {
    private JeiUtil() {
    }

    public static final CStringUtil.ComponentFactory C_JEI_GUI = (s, a) -> translate("jei" + "." + "gui" + "." + Customized.MODID + "." + s, a);
    public static final CStringUtil.ComponentFactory C_JEI_CATEGORY = (s, a) -> translate("jei" + "." + "category" + "." + Customized.MODID + "." + s, a);
    public static final CStringUtil.ComponentFactory C_JEI_INFO = (s, a) -> translate("jei" + "." + "info" + "." + Customized.MODID + "." + s, a);

    public static final int SLOT_SIZE = 16 + 2;
    public static final int TICKS_PER_CYCLE = 200;
    public static final IIngredientType<NutrientCategoryRecipe> NUTRIENT_INGREDIENT = () -> NutrientCategoryRecipe.class;
    public static final RecipeType<PropertyValueRecipeCategory.PropertyValueRecipeAdapter> PROPERTY_VALUE = RecipeType.create(Customized.MODID, "property_value", PropertyValueRecipeCategory.PropertyValueRecipeAdapter.class);
    public static final RecipeType<NutrientBuffRecipe> NUTRIENT_BUFF = RecipeType.create(Customized.MODID, "nutrient_buff", NutrientBuffRecipe.class);
    public static final RecipeType<CasseroleRecipe> CASSEROLE = RecipeType.create(Customized.MODID, "casserole", CasseroleRecipe.class);
    public static final RecipeType<RoasterRecipe> ROASTER = RecipeType.create(Customized.MODID, "roaster", RoasterRecipe.class);
    public static final RecipeType<BrewingBarrelRecipe> BREWING_BARREL = RecipeType.create(Customized.MODID, "brewing_barrel", BrewingBarrelRecipe.class);
    public static final RecipeType<CrateRecipe> CRATE = RecipeType.create(Customized.MODID, "crate", CrateRecipe.class);
    public static final RecipeType<CuttingBoardRecipe> CUTTING_BOARD = RecipeType.create(Customized.MODID, "cutting_board", CuttingBoardRecipe.class);

    public static <I extends RecipeInput, T extends Recipe<I>> List<T> getRecipeList(net.minecraft.world.item.crafting.RecipeType<T> recipeType) {
        return getAllRecipeList(recipeType, Minecraft.getInstance().level);
    }

    public static boolean canAddTooltip(double mouseX, double mouseY, double xStart, double yStart, double width, double height) {
        return mouseX >= xStart && mouseX <= xStart + width && mouseY >= yStart && mouseY <= yStart + height;
    }

    public static void setPotRecipe(IRecipeLayoutBuilder builder, AbstractPotRecipe recipe,
                                    int slotSize, int ingredientCount, int seasoningCount, int spiceCount, int lineSpacing,
                                    int ingredientXStart, int ingredientYStart,
                                    int spiceXStart, int spiceYStart,
                                    int containerXStart, int containerYStart,
                                    int outputXStart, int outputYStart) {
        addIngredientSlots(builder, recipe.getIngredientItem(), RecipeIngredientRole.INPUT,
                ingredientXStart, ingredientYStart, slotSize, 1, ingredientCount, 0, null);
        addIngredientSlots(builder, recipe.getSeasoningItem(), RecipeIngredientRole.INPUT,
                ingredientXStart, slotSize + lineSpacing + ingredientYStart, slotSize, 1, seasoningCount, 0, null);
        addIngredientSlots(builder, recipe.getSpiceItem(), RecipeIngredientRole.INPUT,
                spiceXStart, spiceYStart, slotSize, 1, spiceCount, 0, null);
        builder.addSlot(RecipeIngredientRole.INPUT, containerXStart, containerYStart).addIngredients(recipe.getContainerItem());
        builder.addSlot(RecipeIngredientRole.OUTPUT, outputXStart, outputYStart).addItemStack(recipe.getOutput());
    }

    public static void addCustomIngredientSlots(IRecipeLayoutBuilder builder, int ingredientSize,
                                                int startX, int startY,
                                                int slotSize, int rows, int cols, int spacing,
                                                @Nullable QuadConsumer<IRecipeLayoutBuilder, Integer, Integer, Integer> slotConfigurator) {
        cols = rows <= 0 ? Math.max(1, cols) : (cols <= 0 ? (int) Math.ceil((double) ingredientSize / (double) rows) : cols);
        for (int i = 0; i < ((rows <= 0 || cols <= 0) ? ingredientSize : Math.min(ingredientSize, rows * cols)); i++) {
            int row = i / cols;
            int col = i % cols;
            int x = startX + col * (slotSize + spacing);
            int y = startY + row * (slotSize + spacing);
            if (slotConfigurator != null) {
                slotConfigurator.accept(builder, i, x, y);
            }
        }
    }

    @FunctionalInterface
    public interface QuadConsumer<T, U, V, W> {
        void accept(T t, U u, V v, W w);
    }

    public static void addCustomIngredientSlots(IRecipeLayoutBuilder builder, int ingredientSize,
                                                RecipeIngredientRole role, int startX, int startY,
                                                int slotSize, int rows, int cols, int spacing,
                                                BiConsumer<IRecipeSlotBuilder, Integer> slotConfigurator) {
        addCustomIngredientSlots(builder, ingredientSize, startX, startY, slotSize, rows, cols, spacing,
                (b, i, x, y) -> slotConfigurator.accept(b.addSlot(role, x, y), i));
    }

    public static void addIngredientSlots(IRecipeLayoutBuilder builder, List<Ingredient> ingredients,
                                          RecipeIngredientRole role, int startX, int startY,
                                          int slotSize, int rows, int cols, int spacing,
                                          @Nullable BiConsumer<IRecipeSlotBuilder, Integer> slotConfigurator) {
        addCustomIngredientSlots(builder, ingredients.size(), role, startX, startY, slotSize, rows, cols, spacing, (b, i) -> {
            b.addIngredients(ingredients.get(i));
            if (slotConfigurator != null) {
                slotConfigurator.accept(b, i);
            }
        });
    }

    public static void addProbabilityItemStackSlots(IRecipeLayoutBuilder builder, List<ProbabilityItemStack> itemStacks,
                                          RecipeIngredientRole role, int startX, int startY,
                                          int slotSize, int rows, int cols, int spacing,
                                          @Nullable BiConsumer<IRecipeSlotBuilder, Integer> slotConfigurator) {
        addCustomIngredientSlots(builder, itemStacks.size(), role, startX, startY, slotSize, rows, cols, spacing,  (b, i) -> {
            b.addItemStacks(itemStacks.get(i).probabilityStackList())
                    .addRichTooltipCallback((s, tB) -> tB.add(getProbabilityComponent(itemStacks.get(i).probability())));
            if (slotConfigurator != null) {
                slotConfigurator.accept(b, i);
            }
        });
    }

    public static void addNutrientSlots(IRecipeLayoutBuilder builder, List<NutrientCategoryRecipe> nutrient,
                                        RecipeIngredientRole role, int startX, int startY,
                                        int slotSize, int rows, int cols, int spacing,
                                        @Nullable BiConsumer<IRecipeSlotBuilder, Integer> slotConfigurator) {
        addCustomIngredientSlots(builder, nutrient.size(), role, startX, startY, slotSize, rows, cols, spacing, (b, i) -> {
            b.addIngredient(NUTRIENT_INGREDIENT, nutrient.get(i));
            if (slotConfigurator != null) {
                slotConfigurator.accept(b, i);
            }
        });
    }

    public static void setFluidRecipe(IRecipeLayoutBuilder builder, AbstractPotRecipe recipe, int fluidXStart, int fluidYStart,
                                    int fluidWidth, int fluidHeight, IDrawable overlay, int xOffset, int yOffset) {
        List<FluidStack> fluidStacks = Arrays.stream(recipe.getFluidIngredient().getStacks()).toList();
        fluidStacks.forEach(fluidStack -> {
            IRecipeSlotBuilder fluidSlot = builder.addSlot(RecipeIngredientRole.INPUT, fluidXStart, fluidYStart);
            fluidSlot.setFluidRenderer(FluidType.BUCKET_VOLUME, false, fluidWidth, fluidHeight);
            fluidSlot.setOverlay(overlay, xOffset, yOffset);
            fluidSlot.addFluidStack(fluidStack.getFluid(), fluidStack.getAmount(), fluidStack.getComponentsPatch());
        });
    }

    public static void addPotTooltip(AbstractPotRecipe recipe, double mouseX, double mouseY, List<Component> tooltipString,
                                     int timeXStart, int timeYStart, int timeWidth, int timeHeight,
                                     int fireXStart, int fireYStart, int fireWidth, int fireHeight,
                                     int containerXStart, int containerYStart, int containerWidth, int containerHeight) {
        addTooltipIfInArea(mouseX, mouseY, tooltipString, timeXStart, timeYStart, timeWidth, timeHeight, C_JEI_GUI.create("cook_time", recipe.getCookingTime() / 20));
        if (recipe.isHeated()) {
            addTooltipIfInArea(mouseX, mouseY, tooltipString, fireXStart, fireYStart, fireWidth, fireHeight, C_JEI_GUI.create("fire"));
        }
        addTooltipIfInArea(mouseX, mouseY, tooltipString, containerXStart, containerYStart, containerWidth, containerHeight, C_JEI_GUI.create("container"));
    }

    public static void addTooltipIfInArea(double mouseX, double mouseY, List<Component> tooltipString,
                                          int xStart, int yStart, int width, int height, Component... components) {
        if (canAddTooltip(mouseX, mouseY, xStart, yStart, width, height)) {
            tooltipString.addAll(Arrays.asList(components));
        }
    }

    public static void addJeiInfo(IRecipeRegistration registration, Supplier<Item> item) {
        registration.addIngredientInfo(new ItemStack(item.get()), VanillaTypes.ITEM_STACK,
                C_JEI_INFO.create(BuiltInRegistries.ITEM.getKey(item.get()).getPath()));
    }

    public static void registerJeiInfoForItemTag(IRecipeRegistration registration, TagKey<Item> itemTag) {
        List<ItemStack> stackList = BuiltInRegistries.ITEM.getTag(itemTag).stream().flatMap(HolderSet.ListBacked::stream)
                .map(holder -> new ItemStack(holder.value())).toList();
        if (stackList.isEmpty()) return;
        Component description = C_JEI_INFO.create(itemTag.location().getPath().replace('/', '.'));
        registration.addIngredientInfo(stackList, VanillaTypes.ITEM_STACK, description);
    }

    public static MutableComponent getProbabilityComponent(float probability) {
        return C_JEI_GUI.create("probability", formatPercent(probability, 2)).withStyle(ChatFormatting.AQUA);
    }
}
