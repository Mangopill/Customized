package mangopill.customized.integration.jei.util;

import mangopill.customized.Customized;
import mangopill.customized.common.item.crafting.ProbabilityItemStack;
import mangopill.customized.common.recipe.*;
import mangopill.customized.common.util.*;
import mangopill.customized.integration.jei.category.PropertyValueRecipeCategory;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.*;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.*;
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
    private JeiUtil() {}

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


    /**
     * Retrieves all recipes of a specific type from the current level.
     * <p>
     * This method uses the current Minecraft client level to fetch all available recipes
     * of the specified recipe type. It delegates to {@link RecipeUtil#getAllRecipeList(net.minecraft.world.item.crafting.RecipeType, net.minecraft.world.level.Level)}.
     * @param recipeType The type of recipes to retrieve
     * @param <I> The type of recipe input
     * @param <T> The type of recipe
     * @return A list of all recipes of the specified type in the current level
     */
    public static <I extends RecipeInput, T extends Recipe<I>> List<T> getRecipeList(net.minecraft.world.item.crafting.RecipeType<T> recipeType) {
        return getAllRecipeList(recipeType, Minecraft.getInstance().level);
    }

    /**
     * Checks if the mouse cursor is within a specified rectangular area.
     * <p>
     * This method is used to determine if tooltips should be displayed based on mouse position.
     * @param mouseX The current X coordinate of the mouse cursor
     * @param mouseY The current Y coordinate of the mouse cursor
     * @param xStart The X coordinate of the rectangle's starting point
     * @param yStart The Y coordinate of the rectangle's starting point
     * @param width The width of the rectangle
     * @param height The height of the rectangle
     * @return true if the mouse cursor is within the specified rectangle, false otherwise
     */
    public static boolean canAddTooltip(double mouseX, double mouseY, double xStart, double yStart, double width, double height) {
        return mouseX >= xStart && mouseX <= xStart + width && mouseY >= yStart && mouseY <= yStart + height;
    }


    /**
     * Configures the JEI recipe layout for pot-based recipes with specific slot arrangements.
     * <p>
     * This method sets up input and output slots for pot recipes, including ingredients,
     * seasonings, spices, container items, and the final output. Each slot type is placed
     * at specified coordinates with appropriate spacing.
     * @param builder The IRecipeLayoutBuilder used to construct the recipe layout
     * @param recipe The pot recipe to display
     * @param slotSize The size of each slot (typically 18 pixels including borders)
     * @param ingredientCount The number of ingredient input slots
     * @param seasoningCount The number of seasoning input slots
     * @param spiceCount The number of spice input slots
     * @param lineSpacing Vertical spacing between different slot lines
     * @param ingredientXStart X coordinate for ingredient slots
     * @param ingredientYStart Y coordinate for ingredient slots
     * @param spiceXStart X coordinate for spice slots
     * @param spiceYStart Y coordinate for spice slots
     * @param containerXStart X coordinate for container input slot
     * @param containerYStart Y coordinate for container input slot
     * @param outputXStart X coordinate for output slot
     * @param outputYStart Y coordinate for output slot
     */
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

    /**
     * Adds custom ingredient slots to the JEI recipe layout with flexible configuration.
     * <p>
     * This method provides a flexible way to add slots in a grid arrangement with configurable
     * rows and columns. Each slot can be configured using a custom callback function.
     * @param builder The IRecipeLayoutBuilder used to construct the recipe layout
     * @param ingredientSize The total number of ingredients to display
     * @param startX The starting X coordinate for slot placement
     * @param startY The starting Y coordinate for slot placement
     * @param slotSize The size of each slot (typically 18 pixels including borders)
     * @param rows The number of rows in the grid (0 or negative means dynamic calculation)
     * @param cols The number of columns in the grid (0 or negative means dynamic calculation)
     * @param spacing The spacing between slots
     * @param slotConfigurator A callback function to configure each slot (position, index, etc.)
     */
    public static void addCustomIngredientSlots(IRecipeLayoutBuilder builder, int ingredientSize,
                                                int startX, int startY,
                                                int slotSize, int rows, int cols, int spacing,
                                                @Nullable FunctionalHelper.QuadConsumer<IRecipeLayoutBuilder, Integer, Integer, Integer> slotConfigurator) {
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

    /**
     * Adds custom ingredient slots with specified RecipeIngredientRole.
     * <p>
     * This is a convenience method that wraps {@link #addCustomIngredientSlots(IRecipeLayoutBuilder, int, int, int, int, int, int, int, FunctionalHelper.QuadConsumer)}
     * with role-based slot creation and individual slot configuration.
     * @param builder The IRecipeLayoutBuilder used to construct the recipe layout
     * @param ingredientSize The total number of ingredients to display
     * @param role The role of the ingredients (INPUT, OUTPUT, etc.)
     * @param startX The starting X coordinate for slot placement
     * @param startY The starting Y coordinate for slot placement
     * @param slotSize The size of each slot (typically 18 pixels including borders)
     * @param rows The number of rows in the grid
     * @param cols The number of columns in the grid
     * @param spacing The spacing between slots
     * @param slotConfigurator A callback function to configure each IRecipeSlotBuilder
     */
    public static void addCustomIngredientSlots(IRecipeLayoutBuilder builder, int ingredientSize,
                                                RecipeIngredientRole role, int startX, int startY,
                                                int slotSize, int rows, int cols, int spacing,
                                                BiConsumer<IRecipeSlotBuilder, Integer> slotConfigurator) {
        addCustomIngredientSlots(builder, ingredientSize, startX, startY, slotSize, rows, cols, spacing,
                (b, i, x, y) -> slotConfigurator.accept(b.addSlot(role, x, y), i));
    }


    /**
     * Adds ingredient slots to the JEI recipe layout from a list of Ingredient objects.
     * <p>
     * This method arranges ingredients in a grid and provides optional per-slot configuration.
     * It's commonly used for standard recipe displays with multiple input ingredients.
     * @param builder The IRecipeLayoutBuilder used to construct the recipe layout
     * @param ingredients The list of Ingredient objects to display
     * @param role The role of the ingredients (INPUT, OUTPUT, etc.)
     * @param startX The starting X coordinate for slot placement
     * @param startY The starting Y coordinate for slot placement
     * @param slotSize The size of each slot (typically 18 pixels including borders)
     * @param rows The number of rows in the grid
     * @param cols The number of columns in the grid
     * @param spacing The spacing between slots
     * @param slotConfigurator An optional callback to further configure each slot
     */
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


    /**
     * Adds slots for lists of ItemStacks to the JEI recipe layout.
     * <p>
     * This method is useful when each slot can contain multiple possible items (like ore dictionary entries).
     * It arranges the slots in a grid similar to {@link #addIngredientSlots(IRecipeLayoutBuilder, List, RecipeIngredientRole, int, int, int, int, int, int, BiConsumer)}.
     * @param builder The IRecipeLayoutBuilder used to construct the recipe layout
     * @param itemStacks The list of ItemStack lists (each inner list represents options for one slot)
     * @param role The role of the items (INPUT, OUTPUT, etc.)
     * @param startX The starting X coordinate for slot placement
     * @param startY The starting Y coordinate for slot placement
     * @param slotSize The size of each slot (typically 18 pixels including borders)
     * @param rows The number of rows in the grid
     * @param cols The number of columns in the grid
     * @param spacing The spacing between slots
     * @param slotConfigurator An optional callback to further configure each slot
     */
    public static void addStackListSlots(IRecipeLayoutBuilder builder, List<List<ItemStack>> itemStacks,
                                                    RecipeIngredientRole role, int startX, int startY,
                                                    int slotSize, int rows, int cols, int spacing,
                                                    @Nullable BiConsumer<IRecipeSlotBuilder, Integer> slotConfigurator) {
        addCustomIngredientSlots(builder, itemStacks.size(), role, startX, startY, slotSize, rows, cols, spacing,  (b, i) -> {
            b.addItemStacks(itemStacks.get(i));
            if (slotConfigurator != null) {
                slotConfigurator.accept(b, i);
            }
        });
    }


    /**
     * Adds slots for ProbabilityItemStack objects with probability tooltips.
     * <p>
     * This method displays items with probability-based outputs and adds rich tooltips
     * showing the probability percentage for each item.
     * @param builder The IRecipeLayoutBuilder used to construct the recipe layout
     * @param itemStacks The list of ProbabilityItemStack objects to display
     * @param role The role of the items (typically OUTPUT for probability-based results)
     * @param startX The starting X coordinate for slot placement
     * @param startY The starting Y coordinate for slot placement
     * @param slotSize The size of each slot (typically 18 pixels including borders)
     * @param rows The number of rows in the grid
     * @param cols The number of columns in the grid
     * @param spacing The spacing between slots
     * @param slotConfigurator An optional callback to further configure each slot
     */
    public static void addProbabilityItemStackSlots(IRecipeLayoutBuilder builder, List<ProbabilityItemStack> itemStacks,
                                          RecipeIngredientRole role, int startX, int startY,
                                          int slotSize, int rows, int cols, int spacing,
                                          @Nullable BiConsumer<IRecipeSlotBuilder, Integer> slotConfigurator) {
        addCustomIngredientSlots(builder, itemStacks.size(), role, startX, startY, slotSize, rows, cols, spacing,  (b, i) -> {
            b.addItemStacks(itemStacks.get(i).probabilityStackList())
                    .addRichTooltipCallback((s, tB) -> tB.add(getProbabilityComponent(itemStacks.get(i).probability(), C_JEI_GUI)));
            if (slotConfigurator != null) {
                slotConfigurator.accept(b, i);
            }
        });
    }


    /**
     * Adds slots for nutrient category recipes arranged in lists.
     * <p>
     * This method is used for recipes that require specific nutrient categories as inputs.
     * Each slot can contain multiple nutrient category options.
     * @param builder The IRecipeLayoutBuilder used to construct the recipe layout
     * @param nutrient The list of nutrient category lists (each inner list represents options for one slot)
     * @param role The role of the nutrient inputs (typically INPUT)
     * @param startX The starting X coordinate for slot placement
     * @param startY The starting Y coordinate for slot placement
     * @param slotSize The size of each slot (typically 18 pixels including borders)
     * @param rows The number of rows in the grid
     * @param cols The number of columns in the grid
     * @param spacing The spacing between slots
     * @param slotConfigurator An optional callback to further configure each slot
     */
    public static void addNutrientListSlots(IRecipeLayoutBuilder builder, List<List<NutrientCategoryRecipe>> nutrient,
                                            RecipeIngredientRole role, int startX, int startY,
                                            int slotSize, int rows, int cols, int spacing,
                                            @Nullable BiConsumer<IRecipeSlotBuilder, Integer> slotConfigurator) {
        addCustomIngredientSlots(builder, nutrient.size(), role, startX, startY, slotSize, rows, cols, spacing, (b, i) -> {
            b.addIngredients(NUTRIENT_INGREDIENT, nutrient.get(i));
            if (slotConfigurator != null) {
                slotConfigurator.accept(b, i);
            }
        });
    }


    /**
     * Adds slots for individual nutrient category recipes.
     * <p>
     * Similar to {@link #addNutrientListSlots(IRecipeLayoutBuilder, List, RecipeIngredientRole, int, int, int, int, int, int, BiConsumer)}
     * but for single nutrient category per slot rather than lists of options.
     * @param builder The IRecipeLayoutBuilder used to construct the recipe layout
     * @param nutrient The list of NutrientCategoryRecipe objects to display
     * @param role The role of the nutrient inputs (typically INPUT)
     * @param startX The starting X coordinate for slot placement
     * @param startY The starting Y coordinate for slot placement
     * @param slotSize The size of each slot (typically 18 pixels including borders)
     * @param rows The number of rows in the grid
     * @param cols The number of columns in the grid
     * @param spacing The spacing between slots
     * @param slotConfigurator An optional callback to further configure each slot
     */
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

    /**
     * Configures fluid ingredient slots for pot recipes.
     * <p>
     * This method sets up fluid input slots with appropriate rendering and overlay.
     * It handles fluid stack rendering with custom dimensions and overlay positioning.
     * @param builder The IRecipeLayoutBuilder used to construct the recipe layout
     * @param recipe The pot recipe containing fluid ingredients
     * @param fluidXStart The X coordinate for the fluid slot
     * @param fluidYStart The Y coordinate for the fluid slot
     * @param fluidWidth The width of the fluid display area
     * @param fluidHeight The height of the fluid display area
     * @param overlay The overlay drawable to display on top of the fluid
     * @param xOffset X offset for the overlay position
     * @param yOffset Y offset for the overlay position
     */
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


    /**
     * Adds tooltips for pot recipe GUI elements based on mouse position.
     * <p>
     * This method checks if the mouse is over specific areas (cooking time, fire requirement,
     * container requirement) and adds appropriate tooltips to the tooltip list.
     * @param recipe The pot recipe being displayed
     * @param mouseX The current X coordinate of the mouse cursor
     * @param mouseY The current Y coordinate of the mouse cursor
     * @param tooltipString The list to which tooltips should be added
     * @param timeXStart X coordinate of the cooking time display area
     * @param timeYStart Y coordinate of the cooking time display area
     * @param timeWidth Width of the cooking time display area
     * @param timeHeight Height of the cooking time display area
     * @param fireXStart X coordinate of the fire requirement display area
     * @param fireYStart Y coordinate of the fire requirement display area
     * @param fireWidth Width of the fire requirement display area
     * @param fireHeight Height of the fire requirement display area
     * @param containerXStart X coordinate of the container requirement display area
     * @param containerYStart Y coordinate of the container requirement display area
     * @param containerWidth Width of the container requirement display area
     * @param containerHeight Height of the container requirement display area
     */
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


    /**
     * Adds tooltips to the tooltip list if the mouse is within a specified area.
     * <p>
     * This is a helper method used by {@link #addPotTooltip(AbstractPotRecipe, double, double, List, int, int, int, int, int, int, int, int, int, int, int, int)}
     * and other tooltip methods to conditionally add tooltips based on mouse position.
     * @param mouseX The current X coordinate of the mouse cursor
     * @param mouseY The current Y coordinate of the mouse cursor
     * @param tooltipString The list to which tooltips should be added
     * @param xStart X coordinate of the tooltip area
     * @param yStart Y coordinate of the tooltip area
     * @param width Width of the tooltip area
     * @param height Height of the tooltip area
     * @param components The tooltip components to add if the mouse is within the area
     */
    public static void addTooltipIfInArea(double mouseX, double mouseY, List<Component> tooltipString,
                                          int xStart, int yStart, int width, int height, Component... components) {
        if (canAddTooltip(mouseX, mouseY, xStart, yStart, width, height)) {
            tooltipString.addAll(Arrays.asList(components));
        }
    }

    /**
     * Registers JEI information for a specific item.
     * <p>
     * This method adds descriptive information to an item in JEI's recipe viewer.
     * The information is retrieved from the language files using the item's registry name.
     * @param registration The IRecipeRegistration used to register JEI information
     * @param item A Supplier providing the item to register information for
     */
    public static void addJeiInfo(IRecipeRegistration registration, Supplier<Item> item, CStringUtil.ComponentFactory factory) {
        registration.addIngredientInfo(new ItemStack(item.get()), VanillaTypes.ITEM_STACK,
                factory.create(BuiltInRegistries.ITEM.getKey(item.get()).getPath()));
    }

    /**
     * Registers JEI information for all items in a specific tag using a custom component factory.
     * <p>
     * This method adds descriptive information to all items belonging to a given item tag.
     * The information is generated using the provided component factory, allowing for
     * custom localization or formatting of the description text.
     * @param registration The IRecipeRegistration used to register JEI information
     * @param itemTag The tag key identifying the group of items
     * @param factory The ComponentFactory used to create the description component
     */
    public static void registerJeiInfoForItemTag(IRecipeRegistration registration, TagKey<Item> itemTag, CStringUtil.ComponentFactory factory) {
        List<ItemStack> stackList = BuiltInRegistries.ITEM.getTag(itemTag).stream().flatMap(HolderSet.ListBacked::stream)
                .map(holder -> new ItemStack(holder.value())).toList();
        if (stackList.isEmpty()) return;
        Component description = factory.create(itemTag.location().getPath().replace('/', '.'));
        registration.addIngredientInfo(stackList, VanillaTypes.ITEM_STACK, description);
    }

    /**
     * Creates a formatted probability component for tooltips using a custom component factory.
     * <p>
     * This method generates a colored text component displaying a probability percentage.
     * The percentage is formatted to two decimal places and displayed in aqua color.
     * @param probability The probability value (0.0 to 1.0)
     * @param factory The ComponentFactory used to create the probability text
     * @return A MutableComponent displaying the probability as a percentage
     */
    public static MutableComponent getProbabilityComponent(float probability, CStringUtil.ComponentFactory factory) {
        return factory.create("probability", formatPercent(probability, 2)).withStyle(ChatFormatting.AQUA);
    }

    /**
     * Retrieves IRecipeSlotDrawable objects by name pattern from a collection.
     * <p>
     * This method searches for slot drawables with names following the pattern "name + index"
     * (e.g., "output0", "output1", etc.) and returns them in a list.
     * @param collection The collection to determine the number of slots to search for
     * @param view The IRecipeSlotDrawablesView containing all slot drawables
     * @param name The base name pattern to search for
     * @return A list of IRecipeSlotDrawable objects matching the name pattern
     */
    public static List<IRecipeSlotDrawable> getIRecipeSlotDrawableByName(Collection<?> collection, IRecipeSlotDrawablesView view, String name) {
        if (collection.isEmpty()) return new ArrayList<>();
        List<IRecipeSlotDrawable> drawableList = new ArrayList<>();
        for (int i = 0; i < collection.size(); i++) {
            Optional<IRecipeSlotDrawable> optional = view.findSlotByName(name + i);
            if (optional.isEmpty()) continue;
            drawableList.add(optional.get());
        }
        return drawableList;
    }
}
