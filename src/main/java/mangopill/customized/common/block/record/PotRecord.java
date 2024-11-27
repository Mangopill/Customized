package mangopill.customized.common.block.record;

import mangopill.customized.common.block.entity.AbstractPotBlockEntity;
import mangopill.customized.common.recipe.AbstractPotRecipe;
import mangopill.customized.common.registry.ModBlockEntityTypeRegistry;
import mangopill.customized.common.registry.ModRecipeRegistry;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;

public record PotRecord(int ingredientCount,
                        int seasoningCount,
                        RecipeManager.CachedCheck<RecipeWrapper, ? extends AbstractPotRecipe> potCheck,
                        BlockEntityType<? extends AbstractPotBlockEntity> entityType) {
    public static final PotRecord CASSEROLE = new PotRecord(6, 6,
            RecipeManager.createCheck(ModRecipeRegistry.CASSEROLE.get()), ModBlockEntityTypeRegistry.CASSEROLE.get());
}