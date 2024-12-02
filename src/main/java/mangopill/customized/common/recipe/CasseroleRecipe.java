package mangopill.customized.common.recipe;

import mangopill.customized.common.block.record.PotRecord;
import mangopill.customized.common.registry.ModRecipeRegistry;
import mangopill.customized.common.registry.ModRecipeSerializerRegistry;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public class CasseroleRecipe extends AbstractPotRecipe {
    public CasseroleRecipe(NonNullList<Ingredient> ingredientItem, NonNullList<Ingredient> seasoningItem, Ingredient spiceItem, ItemStack output, int cookingTime) {
        super(ingredientItem, seasoningItem, spiceItem, output, cookingTime,
                ModRecipeSerializerRegistry.CASSEROLE.get(), ModRecipeRegistry.CASSEROLE.get(),
                PotRecord.CASSEROLE.ingredientCount(), PotRecord.CASSEROLE.seasoningCount());
    }
}
