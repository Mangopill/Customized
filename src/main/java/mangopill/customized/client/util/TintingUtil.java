package mangopill.customized.client.util;

import mangopill.customized.common.block.AbstractPlateBlock;
import mangopill.customized.common.block.AbstractPotBlock;
import mangopill.customized.common.block.entity.AbstractPlateBlockEntity;
import mangopill.customized.common.block.entity.AbstractPotBlockEntity;
import mangopill.customized.common.item.AbstractPlateItem;
import mangopill.customized.common.recipe.NutrientCategoryRecipe;
import mangopill.customized.common.util.value.PropertyValue;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.*;

import static mangopill.customized.common.util.PropertyValueUtil.*;

@OnlyIn(Dist.CLIENT)
public final class TintingUtil {
    private TintingUtil() {
    }

    public static int getPotWaterColor(BlockAndTintGetter getter, BlockState state, BlockPos pos) {
        if (state.getBlock() instanceof AbstractPotBlock) {
            if (getter.getBlockEntity(pos) instanceof AbstractPotBlockEntity potBlockEntity){
                List<ItemStack> stackList = potBlockEntity.getItemStackListInPot(false, true);
                if (potBlockEntity.getLevel() != null && potBlockEntity.getFluidHandler().getStoredFluid().getFluid().isSame(Fluids.WATER)) {
                    return getMaxValueColor(potBlockEntity.getLevel(), stackList);
                }
            }
        }
        return -1;
    }

    public static int getPlateBlockWaterColor(BlockAndTintGetter getter, BlockState state, BlockPos pos) {
        if (state.getBlock() instanceof AbstractPlateBlock) {
            if (getter.getBlockEntity(pos) instanceof AbstractPlateBlockEntity plateBlockEntity){
                List<ItemStack> stackList = plateBlockEntity.getItemStackListInPlate(true);
                if (plateBlockEntity.getLevel() != null) {
                    return getMaxValueColor(plateBlockEntity.getLevel(), stackList);
                }
            }
        }
        return -1;
    }

    public static int getPlateItemWaterColor(ItemStack stack) {
        if (stack.getItem() instanceof AbstractPlateItem plateItem && plateItem.hasInput(stack)) {
            List<ItemStack> stackList = plateItem.getItemStackListInPlate(stack, true);
            if (Minecraft.getInstance().player != null && Minecraft.getInstance().level != null) {
                return getMaxValueColor(Minecraft.getInstance().level, stackList);
            }
        }
        return -1;
    }

    public static int getMaxValueColor(Level level, List<ItemStack> stackList) {
        Map<String, Float> nutrientSums = new HashMap<>();
        for (ItemStack stack : stackList) {
            PropertyValue propertyValue = getPropertyValue(stack, level);
            if (propertyValue.isEmpty()) {
                continue;
            }
            propertyValue.getValue().forEach((category, value) -> nutrientSums.put(category, nutrientSums.getOrDefault(category, 0.0F) + value));
        }
        String maxCategory = null;
        double maxSum = 0D;
        for (Map.Entry<String, Float> entry : nutrientSums.entrySet()) {
            if (entry.getValue() > maxSum) {
                maxSum = entry.getValue();
                maxCategory = entry.getKey();
            }
        }
        if (maxCategory != null) {
            List<RecipeHolder<NutrientCategoryRecipe>> recipes = getNutrientCategoryByName(level, maxCategory);
            if (!recipes.isEmpty() && recipes.getFirst() != null) {
                return recipes.getFirst().value().getColorWithAlpha();
            }
        }
        return 0xCC3F76E4;
    }
}
