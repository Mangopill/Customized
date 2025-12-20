package mangopill.customized.common.block.record;

import mangopill.customized.common.block.entity.AbstractPotBlockEntity;
import mangopill.customized.common.item.*;
import mangopill.customized.common.recipe.AbstractPotRecipe;
import mangopill.customized.common.registry.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;

import java.util.function.Supplier;

public record PotRecord(int ingredientCount, int seasoningCount, int spiceCount, RecipeManager.CachedCheck<RecipeWrapper, ? extends AbstractPotRecipe> potCheck,
                        BlockEntityType<? extends AbstractPotBlockEntity> entityType, Supplier<AbstractPlateItem> plateItem, Item lidItem, boolean canInputDrive) {
    public static final PotRecord CASSEROLE = new PotRecord(6, 6, 1,
            RecipeManager.createCheck(CRecipeRegistry.CASSEROLE.get()), CBlockEntityTypeRegistry.CASSEROLE.get(),
            () -> (SoupBowlItem) CItemRegistry.SOUP_BOWL.get(), CItemRegistry.CASSEROLE_LID.get(), true);
    public static final PotRecord ROASTER = new PotRecord(5, 5, 1,
            RecipeManager.createCheck(CRecipeRegistry.ROASTER.get()), CBlockEntityTypeRegistry.ROASTER.get(),
            () -> (BakingPanItem) CItemRegistry.BAKING_PAN.get(), Items.GLASS_PANE, false);
    public static final PotRecord WOK = new PotRecord(5, 8, 2,
            RecipeManager.createCheck(CRecipeRegistry.WOK.get()), CBlockEntityTypeRegistry.WOK.get(),
            () -> (DishItem) CItemRegistry.DISH.get(), Items.GLASS_PANE, true);
    public static final PotRecord STEAMER = new PotRecord(5, 4, 1,
            RecipeManager.createCheck(CRecipeRegistry.STEAMER.get()), CBlockEntityTypeRegistry.STEAMER.get(),
            () -> (PlatterItem) CItemRegistry.PLATTER.get(), Items.GLASS_PANE, true);
}