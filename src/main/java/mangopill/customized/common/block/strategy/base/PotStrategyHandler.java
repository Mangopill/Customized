package mangopill.customized.common.block.strategy.base;

import mangopill.customized.common.block.entity.AbstractPotBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import java.util.*;

public class PotStrategyHandler {
    private static volatile PotStrategyHandler registry;
    private final Map<String, IPotInteractionStrategy[]> map = new HashMap<>();

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

    public void registry(String potName, IPotInteractionStrategy... strategy) {
        if (map.containsKey(potName)) return;
        map.put(potName, strategy);
    }

    public ItemInteractionResult useByRegistry(String potName, ItemStack itemStackInHand, BlockState state, Level level, BlockPos pos,
                                   Player player, InteractionHand hand, BlockHitResult result) {
        if (level.isClientSide) return ItemInteractionResult.SUCCESS;
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof AbstractPotBlockEntity potBlockEntity) {
            for (IPotInteractionStrategy strategy : map.get(potName)){
                if (strategy.interact(itemStackInHand, state, level, pos, player, hand, result)) break;
            }
            potBlockEntity.itemStackHandlerChanged();
        }
        return ItemInteractionResult.SUCCESS;
    }

    public Map<String, IPotInteractionStrategy[]> getMap() {
        return map;
    }
}
