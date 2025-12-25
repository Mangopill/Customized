package mangopill.customized.common.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

import static mangopill.customized.common.util.CStringUtil.*;

public final class CDamageTypeRegistry {
    public static final ResourceKey<DamageType> KNIFE_ENTITY = ResourceKey.create(Registries.DAMAGE_TYPE, getCLoc("knife_entity"));

    public static DamageSource getDamageSource(Level level, ResourceKey<DamageType> type, Entity causingEntity, @Nullable Entity directEntity) {
        return new DamageSource(level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(type), causingEntity, directEntity);
    }
}
