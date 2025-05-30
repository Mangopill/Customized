package mangopill.customized.common.registry;

import mangopill.customized.Customized;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static mangopill.customized.common.registry.ModAdvancementRegistry.getId;

public class ModSoundRegistry {
    public static final DeferredRegister<SoundEvent> SOUND_EVENT = DeferredRegister.create(Registries.SOUND_EVENT, Customized.MODID);

    public static final Supplier<SoundEvent> BOILING_WATER_WITHOUT_LID = SOUND_EVENT.register(
            "boiling_water_without_lid", () -> SoundEvent.createVariableRangeEvent(getId("boiling_water_without_lid"))
    );
    public static final Supplier<SoundEvent> BOILING_WATER_WITH_LID = SOUND_EVENT.register(
            "boiling_water_with_lid", () -> SoundEvent.createVariableRangeEvent(getId("boiling_water_with_lid"))
    );
}
