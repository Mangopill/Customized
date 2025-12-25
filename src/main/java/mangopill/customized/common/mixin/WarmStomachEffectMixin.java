package mangopill.customized.common.mixin;

import mangopill.customized.common.registry.CEffectRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.PowderSnowBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PowderSnowBlock.class)
public abstract class WarmStomachEffectMixin {
    @Shadow
    protected abstract VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context);

    @Inject(at = @At("HEAD"), method = "canEntityWalkOnPowderSnow(Lnet/minecraft/world/entity/Entity;)Z", cancellable = true)
    private static void customized$warmStomachEffect(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (entity instanceof LivingEntity livingEntity && livingEntity.hasEffect(CEffectRegistry.WARM_STOMACH)) {
            cir.setReturnValue(true);
        }
    }
}
