package mangopill.customized.common.item;

import mangopill.customized.common.entity.projectile.KnifeEntity;
import mangopill.customized.common.registry.CSoundRegistry;
import net.minecraft.core.*;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.*;
import net.minecraft.stats.Stats;
import net.minecraft.util.Unit;
import net.minecraft.world.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.*;
import net.minecraft.world.level.storage.loot.parameters.*;

import java.util.List;

import static mangopill.customized.common.util.CItemStackHandlerHelper.*;
import static mangopill.customized.common.util.CStringUtil.*;
import static mangopill.customized.common.util.InteractUtil.*;
import static net.minecraft.world.item.BowItem.*;

public class KnifeItem extends TieredItem implements ProjectileItem, ThrowableIItem {
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
        if (!level.isClientSide && target instanceof Animal animal && attacker.getRandom().nextFloat() < chanceLevel) {
            LootParams.Builder builder = new LootParams.Builder((ServerLevel) level)
                    .withParameter(LootContextParams.THIS_ENTITY, animal)
                    .withParameter(LootContextParams.ORIGIN, animal.position())
                    .withParameter(LootContextParams.DAMAGE_SOURCE, attacker.damageSources().mobAttack(attacker))
                    .withOptionalParameter(LootContextParams.ATTACKING_ENTITY, attacker)
                    .withOptionalParameter(LootContextParams.DIRECT_ATTACKING_ENTITY, attacker);
            if (attacker instanceof Player player) {
                builder = builder.withParameter(LootContextParams.LAST_DAMAGE_PLAYER, player)
                        .withLuck(player.getLuck()).withOptionalParameter(LootContextParams.LAST_DAMAGE_PLAYER, player);
            }
            LootParams lootParams = builder.create(LootContextParamSets.ENTITY);
            ResourceKey<LootTable> lootTableId = animal.getLootTable();
            LootTable lootTable = level.getServer().reloadableRegistries().getLootTable(lootTableId);
            List<ItemStack> lootItems = lootTable.getRandomItems(lootParams);
            lootItems.forEach(itemStack -> itemStack.setCount(1));
            spawnItemEntityList(level, lootItems, null, animal.blockPosition());
            level.playSound(null, animal.blockPosition(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.5F, 1.0F);
        }
        return true;
    }

    @Override
    public void postHurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        hurtAndBreakItemStack(stack, attacker, 1);
    }

    @Override
    public Projectile asProjectile(Level level, Position position, ItemStack itemStack, Direction direction) {
        return new KnifeEntity(level, position.x(), position.y(), position.z(), itemStack);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity shooter, int timeLeft) {
        int useTime = getUseDuration(stack, shooter) - timeLeft;
        float power = getPowerForTime(useTime);
        if (power < 0.1F) return;
        int knifeCount = (level instanceof ServerLevel serverLevel) ? EnchantmentHelper.processProjectileCount(serverLevel, stack, shooter, 1) : 1;
        hurtAndBreakItemStack(stack, shooter, knifeCount);
        ItemStack projectile = stack.copy();
        int i = (level instanceof ServerLevel serverLevel) ? EnchantmentHelper.processAmmoUse(serverLevel, stack, stack, 1) : 1;
        if (i == 0) {
            projectile.set(DataComponents.INTANGIBLE_PROJECTILE, Unit.INSTANCE);
        } else {
            shrinkItemStack(stack, shooter, 1);
        }
        float spreadPerLevel = 15.0F;
        for (int j = 0; j < knifeCount; j++) {
            float yawOffset = 0.0F;
            float pitchOffset = 0.0F;
            if (j > 0) {
                float totalSpread = (spreadPerLevel * j);
                float spreadFactor = (float) (j - 1) / (float) (knifeCount - 1);
                yawOffset = -totalSpread / 2.0F + (totalSpread * spreadFactor);
                float randomSpread = shooter.getRandom().nextFloat() * 5.0F - 2.5F;
                yawOffset += randomSpread;
                pitchOffset = shooter.getRandom().nextFloat() * 3.0F - 1.5F;
            }
            addKnifeEntity(level, shooter, projectile, i, power, yawOffset, pitchOffset);
        }
        level.playSound(null, shooter.getX(), shooter.getY(), shooter.getZ(), CSoundRegistry.KNIFE_SHOOT.get(), SoundSource.PLAYERS, 0.5F, 1.0F);
        if (shooter instanceof Player player) {
            player.awardStat(Stats.ITEM_USED.get(this));
        }
    }

    protected void addKnifeEntity(Level level, LivingEntity shooter, ItemStack projectile, int i, float power, float yawOffset, float pitchOffset) {
        KnifeEntity knife = new KnifeEntity(level, shooter, projectile);
        knife.pickup = (i == 0 ? AbstractArrow.Pickup.DISALLOWED : AbstractArrow.Pickup.ALLOWED);
        float yaw = shooter.getYRot() + yawOffset;
        float pitch = shooter.getXRot() + pitchOffset;
        knife.shootFromRotation(shooter, pitch, yaw, 0.0F, power * 3.0F, 1.0F);
        level.addFreshEntity(knife);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(itemstack);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.SPEAR;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 72000;
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        tooltipComponents.add(getItemTextLootChanceLevelComponent(chanceLevel));
        tooltipComponents.add(component);
    }
}
