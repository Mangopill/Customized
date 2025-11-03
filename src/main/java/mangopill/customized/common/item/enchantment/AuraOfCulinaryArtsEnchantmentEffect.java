package mangopill.customized.common.item.enchantment;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import mangopill.customized.Customized;
import mangopill.customized.common.item.enchantment.network.PlayerAuraData;
import mangopill.customized.common.registry.CEnchantmentComponentRegistry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.*;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.effects.EnchantmentValueEffect;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootTable;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static mangopill.customized.common.CustomizedConfig.*;
import static mangopill.customized.common.util.LootTableUtil.*;
import static mangopill.customized.common.util.ResourceUtil.*;

@EventBusSubscriber(modid = Customized.MODID)
public class AuraOfCulinaryArtsEnchantmentEffect {
    private static final ResourceKey<LootTable> LOOT_TABLE = ResourceKey.create(Registries.LOOT_TABLE, getCLoc("gameplay/aura_of_culinary_arts"));
    public static final Map<String, CulinaryAuraData> PLAYER_AURA_DATA = new HashMap<>();
    private static final int REFRESH_INTERVAL = 600;

    @SubscribeEvent
    public static void onAuraTick(PlayerTickEvent.Pre event) {
        Player player = event.getEntity();
        Level level = player.level();
        ItemStack helmet = player.getItemBySlot(EquipmentSlot.HEAD);
        if (level.isClientSide) {
            return;
        }
        CulinaryAuraData data = PLAYER_AURA_DATA.computeIfAbsent(player.getUUID().toString(),
                k -> new CulinaryAuraData(0, player.level().getGameTime(), new ArrayList<>()));
        AtomicBoolean foundAura = new AtomicBoolean(false);
        EnchantmentHelper.runIterationOnItem(helmet, (e, l) -> {
            EnchantmentValueEffect effect = e.value().effects().get(CEnchantmentComponentRegistry.AURA_OF_CULINARY_ARTS.get());
            if (effect == null) {
                return;
            }
            foundAura.set(true);
            refreshFoodsOverTime(player, data, l);
        });
        if (!foundAura.get()) {
            PLAYER_AURA_DATA.remove(player.getUUID().toString());
        }
        PacketDistributor.sendToAllPlayers(new PlayerAuraData(new HashMap<>(PLAYER_AURA_DATA)));
    }

    @SubscribeEvent
    public static void onPlayerHurt(LivingDamageEvent.Pre event) {
        if (event.getEntity() instanceof Player player && !player.level().isClientSide()) {
            ItemStack helmet = player.getItemBySlot(EquipmentSlot.HEAD);
            EnchantmentHelper.runIterationOnItem(helmet, (e, l) -> {
                EnchantmentValueEffect effect = e.value().effects().get(CEnchantmentComponentRegistry.AURA_OF_CULINARY_ARTS.get());
                if (effect == null) {
                    return;
                }
                CulinaryAuraData data = PLAYER_AURA_DATA.get(player.getUUID().toString());
                if (data != null && data.activeFoods > 0 && event.getOriginalDamage() > 0) {
                    event.setNewDamage(0.0F);
                    data.activeFoods--;
                    data.lastRefreshTime = player.level().getGameTime();
                    data.getStackList().removeLast();
                    PacketDistributor.sendToAllPlayers(new PlayerAuraData(new HashMap<>(PLAYER_AURA_DATA)));
                    if (!AURA_OF_CULINARY_ARTS_MESSAGE.get()) {
                        return;
                    }
                    addParticles(player, 0.5D, 10, 0.1);
                    playSound(player, SoundEvents.ENCHANTMENT_TABLE_USE);
                    player.displayClientMessage(Component.translatable("message.customized.aura_of_culinary_arts", data.activeFoods), true);
                }
            });
        }
    }

    private static void refreshFoodsOverTime(Player player, CulinaryAuraData data, int l) {
        long currentTime = player.level().getGameTime();
        int refreshInterval = REFRESH_INTERVAL / l;
        if (currentTime - data.lastRefreshTime > refreshInterval && data.activeFoods < l) {
            data.activeFoods++;
            data.lastRefreshTime = currentTime;
            data.getStackList().addLast(getRandomLootTableItemStack((ServerLevel) player.level(), LOOT_TABLE));
            PacketDistributor.sendToAllPlayers(new PlayerAuraData(new HashMap<>(PLAYER_AURA_DATA)));
            if (!AURA_OF_CULINARY_ARTS_MESSAGE.get()) {
                return;
            }
            addParticles(player, 1.0, 5, 0.05);
            playSound(player, SoundEvents.EXPERIENCE_ORB_PICKUP);
            player.displayClientMessage(Component.translatable("message.customized.aura_of_culinary_arts", data.activeFoods), true);
        }
    }

    private static void addParticles(Player player, double x, int particleCount, double speed) {
        if (player.level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.HAPPY_VILLAGER,
                    player.getX(), player.getY() + x, player.getZ(),
                    particleCount, 0.3, 0.3, 0.3, speed);
        }
    }

    private static void playSound(Player player, SoundEvent enchantmentTableUse) {
        if (player instanceof ServerPlayer serverPlayer) {
            serverPlayer.connection.send(new ClientboundSoundPacket(
                    BuiltInRegistries.SOUND_EVENT.wrapAsHolder(enchantmentTableUse),
                    SoundSource.PLAYERS,
                    player.getX(), player.getY(), player.getZ(),
                    1.0F, 1.0F, player.level().getRandom().nextLong()
            ));
        }
    }

    public static class CulinaryAuraData {
        private int activeFoods;
        private long lastRefreshTime;
        private final List<ItemStack> stackList;

        public static final StreamCodec<ByteBuf, CulinaryAuraData> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.VAR_INT,
                CulinaryAuraData::getActiveFoods,
                ByteBufCodecs.VAR_LONG,
                CulinaryAuraData::getLastRefreshTime,
                ByteBufCodecs.fromCodec(Codec.list(ItemStack.OPTIONAL_CODEC)),
                CulinaryAuraData::getStackList,
                CulinaryAuraData::new
        );

        public CulinaryAuraData(int activeFoods, long lastRefreshTime, List<ItemStack> stackList) {
            this.activeFoods = activeFoods;
            this.lastRefreshTime = lastRefreshTime;
            this.stackList = stackList;
        }

        public int getActiveFoods() {
            return activeFoods;
        }

        public long getLastRefreshTime() {
            return lastRefreshTime;
        }

        public List<ItemStack> getStackList() {
            return stackList;
        }
    }
}