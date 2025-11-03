package mangopill.customized.common.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

import java.util.List;

import static mangopill.customized.common.util.CItemStackHandlerHelper.spawnItemEntity;

public class KnifeItem extends TieredItem {
    private final float chanceLevel;

    public KnifeItem(Tier tier, Properties properties, float chanceLevel) {
        super(tier, properties);
        this.chanceLevel = chanceLevel;
    }

    public KnifeItem(Tier tier, Properties properties, Tool toolComponentData, float chanceLevel) {
        super(tier, properties.component(DataComponents.TOOL, toolComponentData));
        this.chanceLevel = chanceLevel;
    }

    @Override
    public boolean canAttackBlock(BlockState state, Level level, BlockPos pos, Player player) {
        return !player.isCreative();
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        Level level = target.level();
        if (!level.isClientSide && attacker.getRandom().nextFloat() < chanceLevel) {
            LootParams.Builder builder = new LootParams.Builder((ServerLevel) level)
                    .withOptionalParameter(LootContextParams.THIS_ENTITY, target instanceof Animal ? target : null)
                    .withParameter(LootContextParams.ORIGIN, target.position())
                    .withParameter(LootContextParams.DAMAGE_SOURCE, attacker.damageSources().mobAttack(attacker))
                    .withOptionalParameter(LootContextParams.ATTACKING_ENTITY, attacker)
                    .withOptionalParameter(LootContextParams.DIRECT_ATTACKING_ENTITY, attacker)
                    .withOptionalParameter(LootContextParams.LAST_DAMAGE_PLAYER, attacker instanceof Player ? (Player) attacker : null);
            if (attacker instanceof Player player) {
                builder = builder.withParameter(LootContextParams.LAST_DAMAGE_PLAYER, player)
                        .withLuck(player.getLuck());
            }
            LootParams lootParams = builder.create(LootContextParamSets.ENTITY);
            ResourceKey<LootTable> lootTableId = target.getLootTable();
            LootTable lootTable = level.getServer().reloadableRegistries().getLootTable(lootTableId);
            List<ItemStack> lootItems = lootTable.getRandomItems(lootParams);
            lootItems.forEach(itemStack -> spawnItemEntity(level, itemStack, null, target.blockPosition()));
            level.playSound(null, target.blockPosition(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.5F, 1.0F);
        }
        return true;
    }

    @Override
    public void postHurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.hurtAndBreak(1, attacker, EquipmentSlot.MAINHAND);
    }
}
