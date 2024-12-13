package mangopill.customized.common.block.strategy.base;

import com.google.common.collect.Maps;
import mangopill.customized.common.block.entity.AbstractPotBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class PotStrategyHandler {
    private static volatile PotStrategyHandler registry;
    private final Map<String, PotInteractionStrategy[]> map = Maps.newHashMap();
    private int count = 0;

    private PotStrategyHandler() {
    }

    public static PotStrategyHandler getInstance() {
        synchronized (PotStrategyHandler.class) {
            if (registry == null) {
                registry = new PotStrategyHandler();
            }
        }
        return registry;
    }

    public void registry(@NotNull String potName, @NotNull PotInteractionStrategy... strategy) {
        if(map.containsKey(potName)){
            return;
        }
        map.put(potName, strategy);
        count++;
    }

    public ItemInteractionResult useByRegistry(String potName, ItemStack itemStackInHand, BlockState state, Level level, BlockPos pos,
                                   Player player, InteractionHand hand, BlockHitResult result) {
        if (level.isClientSide){
            return ItemInteractionResult.SUCCESS;
        }
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof AbstractPotBlockEntity potBlockEntity) {
            if (count == 0){
                PotStrategyRegistry.onPotRegistry();
            }
            for (PotInteractionStrategy strategy : map.get(potName)){
                if (strategy.interact(itemStackInHand, state, level, pos, player, hand, result)) {
                    break;
                }
            }
            potBlockEntity.itemStackHandlerChanged();
        }
        return ItemInteractionResult.SUCCESS;
    }

    public Map<String, PotInteractionStrategy[]> getMap() {
        return map;
    }

    public int getCount() {
        return count;
    }
}
