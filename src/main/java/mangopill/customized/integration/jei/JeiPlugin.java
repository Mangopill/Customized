package mangopill.customized.integration.jei;

import mangopill.customized.Customized;
import mangopill.customized.common.registry.ModItemRegistry;
import mangopill.customized.integration.jei.category.*;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.registration.*;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;

import static mangopill.customized.integration.jei.util.JeiUtil.*;

@mezz.jei.api.JeiPlugin
public class JeiPlugin implements IModPlugin {
    public static final ResourceLocation JEI_PLUGIN_UID = ResourceLocation.fromNamespaceAndPath(Customized.MODID, "jei_plugin");

    @Override
    public void registerCategories(@Nonnull IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new CasseroleRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new BrewingBarrelRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(@Nonnull IRecipeRegistration registration) {
        if (Minecraft.getInstance().level != null) {
            registration.addRecipes(CASSEROLE, getCasseroleRecipeList());
            registration.addRecipes(BREWING_BARREL, getBrewingBarrelRecipeList());
        }
        addJeiInfo(registration, ModItemRegistry.SOILED_SEED, ".soiled_seed");
        addJeiInfo(registration, ModItemRegistry.SOUP_BOWL, ".soup_bowl");
        addJeiInfo(registration, ModItemRegistry.SALT_PAN, ".salt_pan");
        addJeiInfo(registration, ModItemRegistry.SALT, ".salt");
        addJeiInfo(registration, ModItemRegistry.SPOON, ".spoon");
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModItemRegistry.CASSEROLE.get()), RecipeTypes.CAMPFIRE_COOKING, CASSEROLE);
        registration.addRecipeCatalyst(new ItemStack(ModItemRegistry.BREWING_BARREL.get()), BREWING_BARREL);
    }

    @Override
    public @Nonnull ResourceLocation getPluginUid() {
        return JEI_PLUGIN_UID;
    }
}
