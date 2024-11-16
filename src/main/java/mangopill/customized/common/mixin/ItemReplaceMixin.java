package mangopill.customized.common.mixin;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mangopill.customized.Customized;
import mangopill.customized.common.registry.ModParticleTypeRegistry;
import mangopill.customized.common.tag.ModTag;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public abstract class ItemReplaceMixin{
    private static final ResourceKey<LootTable> LOOT_TABLE = ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(Customized.MODID, "entities/soiled_seed"));
    private int life = 200;
    @Inject(at = @At("HEAD"), method = "tick")
    public void customized$itemReplace(CallbackInfo ci) {
        ItemEntity itemEntity = (ItemEntity)(Object)this;
        ItemStack itemStack = itemEntity.getItem().copy();
        if (!(itemStack.is(ModTag.SOILED_SEED)) || !(itemEntity.isInWaterRainOrBubble())) {
            return;
        }
        itemEntity.level().addParticle(ModParticleTypeRegistry.DIRT.get(), itemEntity.getX(), itemEntity.getY(), itemEntity.getZ(), 0.0D, 0.0D, 0.0D);
        if (itemEntity.level().isClientSide() || itemEntity.level().getServer() == null) {
            return;
        }
        LootParams.Builder builder = new LootParams.Builder((ServerLevel) itemEntity.level());
        LootParams params = builder.create(LootContextParamSets.EMPTY);
        LootTable lootTable = itemEntity.level().getServer().reloadableRegistries().getLootTable(LOOT_TABLE);
        if(this.life > 0) {
            this.life--;
        }else {
            int i = 0;
            while (i < itemStack.getCount()){
                ObjectArrayList<ItemStack> objectArrayList = lootTable.getRandomItems(params);
                if (objectArrayList.isEmpty()) {
                    return;
                }
                ItemStack randomItem = objectArrayList.get(itemEntity.level().random.nextInt(objectArrayList.size())).copy();
                ItemEntity newItemEntity = new ItemEntity(itemEntity.level(),
                        itemEntity.getX(),
                        itemEntity.getY(),
                        itemEntity.getZ(),
                        randomItem.copy());
                itemEntity.level().addFreshEntity(newItemEntity);
                i++;
            }
            itemEntity.discard();
        }
    }
}
