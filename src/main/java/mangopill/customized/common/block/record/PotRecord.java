package mangopill.customized.common.block.record;

import mangopill.customized.common.block.entity.AbstractPotBlockEntity;
import mangopill.customized.common.item.AbstractPlateItem;
import mangopill.customized.common.recipe.AbstractPotRecipe;
import mangopill.customized.common.registry.*;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;

public record PotRecord(int ingredientCount,
                        int seasoningCount,
                        int spiceCount,
                        RecipeManager.CachedCheck<RecipeWrapper, ? extends AbstractPotRecipe> potCheck,
                        BlockEntityType<? extends AbstractPotBlockEntity> entityType, AbstractPlateItem plateItem) {
    public static final PotRecord CASSEROLE = new PotRecord(6, 6, 1,
            RecipeManager.createCheck(CRecipeRegistry.CASSEROLE.get()), CBlockEntityTypeRegistry.CASSEROLE.get(), (AbstractPlateItem) CItemRegistry.SOUP_BOWL.get());
}