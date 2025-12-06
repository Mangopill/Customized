package mangopill.customized.common.recipe;

import mangopill.customized.common.block.record.PotRecord;
import mangopill.customized.common.registry.CRecipeRegistry;
import mangopill.customized.common.registry.CRecipeSerializerRegistry;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;

public class RoasterRecipe extends AbstractPotRecipe {
    public RoasterRecipe(NonNullList<Ingredient> ingredientItem, NonNullList<Ingredient> seasoningItem, NonNullList<Ingredient> spiceItem, Ingredient containerItem, FluidIngredient fluidIngredient, ItemStack output, int cookingTime, boolean heated) {
        super(ingredientItem, seasoningItem, spiceItem, containerItem, fluidIngredient, output, cookingTime, heated,
                CRecipeSerializerRegistry.ROASTER.get(), CRecipeRegistry.ROASTER.get(),
                PotRecord.ROASTER.ingredientCount(), PotRecord.ROASTER.seasoningCount());
    }
}
