package mangopill.customized.common.entity.projectile;

import mangopill.customized.common.item.KnifeItem;
import mangopill.customized.common.registry.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

import static mangopill.customized.common.registry.CDamageTypeRegistry.*;
import static mangopill.customized.common.util.CItemStackHandlerHelper.addItemToPlayerNotCreative;

public class KnifeEntity extends AbstractArrow {

    public KnifeEntity(EntityType<? extends AbstractArrow> entityType, Level level) {
        super(entityType, level);
    }

    public KnifeEntity(Level level, double x, double y, double z, ItemStack pickupItemStack) {
        super(CEntityTypeRegistry.KNIFE.get(), x, y, z, level, pickupItemStack, null);
    }

    public KnifeEntity(Level level, LivingEntity owner, ItemStack stack) {
        super(CEntityTypeRegistry.KNIFE.get(), owner, level, stack, null);
    }

    @Override
    public ItemStack getWeaponItem() {
        return getPickupItem();
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return CItemRegistry.IRON_KNIFE.get().getDefaultInstance();
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        Entity entity = result.getEntity();
        Level level = level();
        Entity owner = getOwner();
        ItemStack itemStack = getPickupItem();
        float damage;
        if (!(owner instanceof LivingEntity shooter)) {
            return;
        }
        damage = (float) shooter.getAttributeValue(Attributes.ATTACK_DAMAGE);
        if (!(level instanceof ServerLevel serverlevel)) {
            return;
        }
        damage = EnchantmentHelper.modifyDamage(serverlevel, itemStack, entity, getDamageSource(level, KNIFE_ENTITY, this, owner), damage);
        EnchantmentHelper.doPostAttackEffectsWithItemSource(serverlevel, entity, getDamageSource(level, KNIFE_ENTITY, this, owner), getWeaponItem());
        int i = entity.getRemainingFireTicks();
        if (isOnFire() && entity.getType() != EntityType.ENDERMAN) {
            entity.igniteForSeconds(5.0F);
        }
        if (entity.hurt(getDamageSource(level, KNIFE_ENTITY, this, owner), damage)) {
            if (entity instanceof LivingEntity target) {
                if (itemStack.getItem() instanceof KnifeItem knifeItem) {
                    knifeItem.hurtEnemy(itemStack, target, shooter);
                }
                target.setLastHurtMob(entity);
                serverlevel.sendParticles(ParticleTypes.DAMAGE_INDICATOR, target.getX(), target.getY(0.5D), target.getZ(), Math.round(damage), 0.1D, 0.0D, 0.1D, 0.2D);
            }
            if (owner instanceof ServerPlayer serverPlayer) {
                CAdvancementRegistry.USE_FLYING_KNIFE.get().trigger(serverPlayer);
            }
        } else {
            entity.setRemainingFireTicks(i);
        }
        playSound(CSoundRegistry.KNIFE_HIT_ENTITY.get(), 0.1F, 1.0F);
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        setSoundEvent(getDefaultHitGroundSoundEvent());
    }

    @Override
    protected boolean tryPickup(Player player) {
        return switch (pickup) {
            case ALLOWED -> {
                if (getOwner() == null || getOwner().equals(player)) {
                    addItemToPlayerNotCreative(player, getPickupItem());
                    yield true;
                }
                yield false;
            }
            case CREATIVE_ONLY -> true;
            default -> false;
        };
    }

    @Override
    public void tickDespawn() {
        if (pickup != AbstractArrow.Pickup.ALLOWED) {
            super.tickDespawn();
        }
    }

    @Override
    public boolean shouldRender(double cameraX, double cameraY, double cameraZ) {
        return true;
    }

    @Override
    public Component getName() {
        return getPickupItem().getDisplayName();
    }

    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.EMPTY;
    }
}
