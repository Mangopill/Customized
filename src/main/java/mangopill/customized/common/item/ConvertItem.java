package mangopill.customized.common.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

public class ConvertItem extends Item {
    public ConvertItem(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
        if (!this.isEdible()) {
            return stack;
        }
        livingEntity.eat(level, stack);
        if (livingEntity instanceof Player player) {
            if (!player.getInventory().add(stack.getCraftingRemainingItem())) {
                player.drop(stack.getCraftingRemainingItem(), false);
            }
        }
        return stack;
    }
}
