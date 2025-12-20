package mangopill.customized.common.registry;

import mangopill.customized.Customized;
import mangopill.customized.common.entity.projectile.KnifeEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class CEntityTypeRegistry {
    public static final DeferredRegister<EntityType<?>> ENTITY = DeferredRegister.create(Registries.ENTITY_TYPE, Customized.MODID);

    public static final Supplier<EntityType<KnifeEntity>> KNIFE = ENTITY.register("knife", () -> (
            EntityType.Builder.<KnifeEntity>of(KnifeEntity::new, MobCategory.MISC).sized(0.3F, 0.3F)
                    .clientTrackingRange(4).updateInterval(10).build("knife")));
}
