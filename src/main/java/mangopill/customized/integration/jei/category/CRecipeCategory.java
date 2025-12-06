package mangopill.customized.integration.jei.category;

import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public abstract class CRecipeCategory <T> implements IRecipeCategory<T> {
    protected final RecipeType<T> recipeType;
    protected final ResourceLocation image;
    protected Component title;
    protected IDrawable background;
    protected IDrawable icon;
    protected IDrawable fluidOverlay;
    protected IDrawable drive;
    protected IDrawable arrow;

    public CRecipeCategory(RecipeType<T> recipeType, ResourceLocation image) {
        this.recipeType = recipeType;
        this.image = image;
    }

    @Override
    public RecipeType<T> getRecipeType() {
        return recipeType;
    }

    @Override
    public Component getTitle() {
        return title;
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    @Nullable
    public IDrawable getIcon() {
        return icon;
    }
}
