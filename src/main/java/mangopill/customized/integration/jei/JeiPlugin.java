package mangopill.customized.integration.jei;

import mangopill.customized.Customized;
import mangopill.customized.common.registry.CItemRegistry;
import mangopill.customized.common.registry.CRecipeRegistry;
import mangopill.customized.integration.jei.category.*;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.registration.*;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import static mangopill.customized.common.util.ResourceUtil.*;
import static mangopill.customized.integration.jei.util.JeiUtil.*;

@mezz.jei.api.JeiPlugin
public class JeiPlugin implements IModPlugin {
    public static final ResourceLocation JEI_PLUGIN_UID = getCLoc("jei_plugin");

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new CasseroleRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new BrewingBarrelRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new CrateRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new CuttingBoardRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        if (Minecraft.getInstance().level != null) {
            registration.addRecipes(CASSEROLE, getRecipeList(CRecipeRegistry.CASSEROLE.get()));
            registration.addRecipes(BREWING_BARREL, getRecipeList(CRecipeRegistry.BREWING_BARREL.get()));
            registration.addRecipes(CRATE, getRecipeList(CRecipeRegistry.CRATE.get()));
            registration.addRecipes(CUTTING_BOARD, getRecipeList(CRecipeRegistry.CUTTING_BOARD.get()));
        }
        addJeiInfo(registration, CItemRegistry.SOILED_SEED);
        addJeiInfo(registration, CItemRegistry.SOUP_BOWL);
        addJeiInfo(registration, CItemRegistry.SALT_PAN);
        addJeiInfo(registration, CItemRegistry.SALT);
        addJeiInfo(registration, CItemRegistry.SPOON);
        addJeiInfo(registration, CItemRegistry.CRATE);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(CItemRegistry.CASSEROLE.get()), RecipeTypes.CAMPFIRE_COOKING, CASSEROLE);
        registration.addRecipeCatalyst(new ItemStack(CItemRegistry.BREWING_BARREL.get()), BREWING_BARREL);
        registration.addRecipeCatalyst(new ItemStack(CItemRegistry.CRATE.get()), CRATE);
        registration.addRecipeCatalyst(new ItemStack(CItemRegistry.CUTTING_BOARD.get()), CUTTING_BOARD);
    }

    @Override
    public ResourceLocation getPluginUid() {
        return JEI_PLUGIN_UID;
    }
}
