package mangopill.customized.integration.jei;

import mangopill.customized.common.recipe.serializer.NutrientCategorySerializer;
import mangopill.customized.common.registry.CItemRegistry;
import mangopill.customized.common.registry.CRecipeRegistry;
import mangopill.customized.common.tag.ModTag;
import mangopill.customized.integration.jei.category.*;
import mangopill.customized.integration.jei.ingredient.NutrientIngredientHelper;
import mangopill.customized.integration.jei.ingredient.NutrientIngredientRenderer;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.registration.*;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import static mangopill.customized.common.util.StringUtil.*;
import static mangopill.customized.integration.jei.category.PropertyValueRecipeCategory.PropertyValueRecipeAdapter.*;
import static mangopill.customized.integration.jei.util.JeiUtil.*;

@mezz.jei.api.JeiPlugin
public class JeiPlugin implements IModPlugin {
    public static final ResourceLocation JEI_PLUGIN_UID = getCLoc("jei_plugin");

    @Override
    public void registerIngredients(IModIngredientRegistration registration) {
        registration.register(NUTRIENT_INGREDIENT, getRecipeList(CRecipeRegistry.NUTRIENT_CATEGORY.get()), new NutrientIngredientHelper(), new NutrientIngredientRenderer(), NutrientCategorySerializer.CODEC.codec());
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new PropertyValueRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new NutrientBuffRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new CasseroleRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new RoasterRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new BrewingBarrelRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new CrateRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new CuttingBoardRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        if (Minecraft.getInstance().level != null) {
            registration.addRecipes(PROPERTY_VALUE, getPropertyValueList());
            registration.addRecipes(NUTRIENT_BUFF, getRecipeList(CRecipeRegistry.NUTRIENT_BUFF.get()));
            registration.addRecipes(CASSEROLE, getRecipeList(CRecipeRegistry.CASSEROLE.get()));
            registration.addRecipes(ROASTER, getRecipeList(CRecipeRegistry.ROASTER.get()));
            registration.addRecipes(BREWING_BARREL, getRecipeList(CRecipeRegistry.BREWING_BARREL.get()));
            registration.addRecipes(CRATE, getRecipeList(CRecipeRegistry.CRATE.get()));
            registration.addRecipes(CUTTING_BOARD, getRecipeList(CRecipeRegistry.CUTTING_BOARD.get()));
        }
        registerJeiInfoForItemTag(registration, ModTag.SOILED_SEED);
        registerJeiInfoForItemTag(registration, ModTag.CUSTOMIZED_PLATE);
        addJeiInfo(registration, CItemRegistry.SALT_PAN);
        addJeiInfo(registration, CItemRegistry.SALT);
        addJeiInfo(registration, CItemRegistry.SPOON);
        addJeiInfo(registration, CItemRegistry.CRATE);
        registerJeiInfoForItemTag(registration, ModTag.KNIFE);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(CItemRegistry.CASSEROLE.get()), RecipeTypes.CAMPFIRE_COOKING, CASSEROLE);
        registration.addRecipeCatalyst(new ItemStack(CItemRegistry.ROASTER.get()), RecipeTypes.CAMPFIRE_COOKING, ROASTER);
        registration.addRecipeCatalyst(new ItemStack(CItemRegistry.BREWING_BARREL.get()), BREWING_BARREL);
        registration.addRecipeCatalyst(new ItemStack(CItemRegistry.CRATE.get()), CRATE);
        registration.addRecipeCatalyst(new ItemStack(CItemRegistry.CUTTING_BOARD.get()), CUTTING_BOARD);
    }

    @Override
    public ResourceLocation getPluginUid() {
        return JEI_PLUGIN_UID;
    }
}
