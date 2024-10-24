package mangopill.customized.common.mixin;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mangopill.customized.Customized;
import mangopill.customized.common.item.SoiledSeedItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(ItemEntity.class)
public abstract class ItemReplaceMixin{
    private static final ResourceKey<LootTable> LOOT_TABLE = ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(Customized.MODID, "entity/soiled_seed"));
    @Inject(at = @At("HEAD"), method = "tick")
    public void itemReplace(CallbackInfo ci) {
        ItemEntity itemEntity = (ItemEntity)(Object)this;
        ItemStack itemStack = itemEntity.getItem().copy();
        if (!(itemStack.getItem() instanceof SoiledSeedItem) || !(itemEntity.isInWaterRainOrBubble())) {
            return;
        }
        if (itemEntity.level().isClientSide() || itemEntity.level().getServer() == null) {
            return;
        }
        LootParams.Builder builder = new LootParams.Builder((ServerLevel) itemEntity.level());
        LootParams params = builder.create(LootContextParamSets.EMPTY);
        LootTable lootTable = itemEntity.level().getServer().reloadableRegistries().getLootTable(LOOT_TABLE);
        ObjectArrayList<ItemStack> objectArrayList = lootTable.getRandomItems(params);
        if (objectArrayList.isEmpty()) {
            return;
        }
        int i = 0;
        while (i < itemStack.getCount()){
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
