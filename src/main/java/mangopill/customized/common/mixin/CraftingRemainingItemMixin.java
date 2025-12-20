package mangopill.customized.common.mixin;

import mangopill.customized.common.item.KnifeItem;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Item.class)
public abstract class CraftingRemainingItemMixin {
    @Final
    @Shadow
    @Mutable
    private Item craftingRemainingItem;

    @Inject(
            method = {"<init>"},
            at = {@At("TAIL")}
    )
    public void customized$constructor(Item.Properties properties, CallbackInfo ci) {
        Item item = (Item)(Object)this;
        if (item instanceof KnifeItem) {
            craftingRemainingItem = item;
        }
    }
}