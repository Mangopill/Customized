package mangopill.customized.client.util;

import mangopill.customized.common.block.AbstractPlateBlock;
import mangopill.customized.common.block.entity.AbstractPlateBlockEntity;
import mangopill.customized.common.item.AbstractPlateItem;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.*;

import static mangopill.customized.common.util.PropertyValueUtil.*;

@OnlyIn(Dist.CLIENT)
public final class TintingUtil {
    private TintingUtil() {
    }

    public static int getPlateBlockWaterColor(BlockAndTintGetter getter, BlockState state, BlockPos pos) {
        if (!(state.getBlock() instanceof AbstractPlateBlock)) return -1;
        if (!(getter.getBlockEntity(pos) instanceof AbstractPlateBlockEntity plateBlockEntity)) return -1;
        List<ItemStack> stackList = plateBlockEntity.getItemStackListInPlate(true);
        return plateBlockEntity.getLevel() != null ? getMaxValueColor(plateBlockEntity.getLevel(), stackList) : -1;
    }

    public static int getPlateItemWaterColor(ItemStack stack) {
        if (!(stack.getItem() instanceof AbstractPlateItem plateItem) || !plateItem.hasInput(stack)) return -1;
        List<ItemStack> stackList = plateItem.getItemStackListInPlate(stack, true);
        return Minecraft.getInstance().player != null && Minecraft.getInstance().level != null ? getMaxValueColor(Minecraft.getInstance().level, stackList) : -1;
    }
}
