package mangopill.customized.common.registry;

import mangopill.customized.Customized;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static mangopill.customized.common.util.ResourceUtil.*;

public class CSoundRegistry {
    public static final DeferredRegister<SoundEvent> SOUND_EVENT = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, Customized.MODID);

    public static final Supplier<SoundEvent> BOILING_WATER_WITHOUT_LID = SOUND_EVENT.register(
            "boiling_water_without_lid", () -> SoundEvent.createVariableRangeEvent(getCLoc("boiling_water_without_lid"))
    );
    public static final Supplier<SoundEvent> BOILING_WATER_WITH_LID = SOUND_EVENT.register(
            "boiling_water_with_lid", () -> SoundEvent.createVariableRangeEvent(getCLoc("boiling_water_with_lid"))
    );
}
