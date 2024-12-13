package mangopill.customized.integration.jei;

import mangopill.customized.Customized;
import mangopill.customized.common.registry.ModItemRegistry;
import mangopill.customized.integration.jei.category.*;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.*;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import static mangopill.customized.integration.jei.util.JeiUtil.*;

@mezz.jei.api.JeiPlugin
public class JeiPlugin implements IModPlugin {
    public static final ResourceLocation JEI_PLUGIN_UID = ResourceLocation.fromNamespaceAndPath(Customized.MODID, "jei_plugin");

    @Override
    public void registerCategories(@NotNull IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new CasseroleRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new BrewingBarrelRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(@NotNull IRecipeRegistration registration) {
        if (Minecraft.getInstance().level != null) {
            registration.addRecipes(CASSEROLE, getCasseroleRecipeList());
            registration.addRecipes(BREWING_BARREL, getBrewingBarrelRecipeList());
        }
        registration.addIngredientInfo(new ItemStack(ModItemRegistry.SOILED_SEED.get()), VanillaTypes.ITEM_STACK,
                Component.translatable("jei.info." + Customized.MODID + ".soiled_seed"));
        registration.addIngredientInfo(new ItemStack(ModItemRegistry.SOUP_BOWL.get()), VanillaTypes.ITEM_STACK,
                Component.translatable("jei.info." + Customized.MODID + ".soup_bowl"));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModItemRegistry.CASSEROLE.get()), RecipeTypes.CAMPFIRE_COOKING, CASSEROLE);
        registration.addRecipeCatalyst(new ItemStack(ModItemRegistry.BREWING_BARREL.get()), BREWING_BARREL);
    }

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return JEI_PLUGIN_UID;
    }
}
