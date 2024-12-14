package mangopill.customized.common.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class DrinkableItem extends Item {
    public DrinkableItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity livingEntity) {
        ItemStack craftingRemainingItem = stack.getCraftingRemainingItem();
        if (livingEntity instanceof Player player) {
            if (!player.getInventory().add(craftingRemainingItem)) {
                player.drop(craftingRemainingItem, false);
            }
        }
        return super.finishUsingItem(stack, level, livingEntity);
    }
}
