package mangopill.customized.common.recipe;

import mangopill.customized.common.block.record.PotRecord;
import mangopill.customized.common.registry.CRecipeRegistry;
import mangopill.customized.common.registry.CRecipeSerializerRegistry;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public class CasseroleRecipe extends AbstractPotRecipe {
    public CasseroleRecipe(NonNullList<Ingredient> ingredientItem, NonNullList<Ingredient> seasoningItem, Ingredient spiceItem, Ingredient containerItem, ItemStack output, int cookingTime) {
        super(ingredientItem, seasoningItem, spiceItem, containerItem, output, cookingTime,
                CRecipeSerializerRegistry.CASSEROLE.get(), CRecipeRegistry.CASSEROLE.get(),
                PotRecord.CASSEROLE.ingredientCount(), PotRecord.CASSEROLE.seasoningCount());
    }
}
