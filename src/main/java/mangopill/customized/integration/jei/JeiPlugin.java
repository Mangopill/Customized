package mangopill.customized.integration.jei;

import mangopill.customized.Customized;
import mangopill.customized.common.registry.ModItemRegistry;
import mangopill.customized.integration.jei.category.CasseroleRecipeCategory;
import mangopill.customized.integration.jei.util.JeiUtil;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.*;
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
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(JeiUtil.CASSEROLE, getCasseroleRecipeList());
        registration.addIngredientInfo(new ItemStack(ModItemRegistry.SOUP_BOWL.get()), VanillaTypes.ITEM_STACK, Component.translatable("jei.info." + Customized.MODID + ".soup_bowl"));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModItemRegistry.CASSEROLE.get()), RecipeTypes.CAMPFIRE_COOKING, JeiUtil.CASSEROLE);
    }

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return JEI_PLUGIN_UID;
    }
}
