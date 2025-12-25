package mangopill.customized.common.mixin;

import mangopill.customized.common.registry.*;
import mangopill.customized.common.tag.ModTag;
import net.minecraft.core.registries.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static mangopill.customized.common.util.CItemStackHandlerHelper.*;
import static mangopill.customized.common.util.CStringUtil.*;
import static mangopill.customized.common.util.LootTableUtil.*;

@Mixin(ItemEntity.class)
public abstract class ItemReplaceMixin{
    @Unique
    private int customized$life = 200;
    @Inject(at = @At("HEAD"), method = "tick")
    public void customized$itemReplace(CallbackInfo ci) {
        ItemEntity itemEntity = (ItemEntity)(Object)this;
        ItemStack itemStack = itemEntity.getItem().copy();
        Level level = itemEntity.level();
        if (!(itemStack.is(ModTag.SOILED_SEED)) || !(itemEntity.isInWaterRainOrBubble())) return;
        level.addParticle(CParticleTypeRegistry.DIRT.get(), itemEntity.getX(), itemEntity.getY(), itemEntity.getZ(), 0.0D, 0.0D, 0.0D);
        if (level.isClientSide() || level.getServer() == null) return;
        if (this.customized$life > 0) {
            this.customized$life--;
        } else {
            int i = 0;
            while (i < itemStack.getCount()){
                spawnItemEntity(level, getRandomLootTableItemStack((ServerLevel) level, ResourceKey.create(Registries.LOOT_TABLE,
                        getCLoc("gameplay/" + BuiltInRegistries.ITEM.getKey(itemStack.getItem()).getPath()))), null, itemEntity.getOnPos().below());
                i++;
            }
            itemEntity.discard();
            if (itemEntity.getOwner() instanceof ServerPlayer owner) {
                CAdvancementRegistry.WASH_SEEDS.get().trigger(owner);
            }
        }
    }
}
