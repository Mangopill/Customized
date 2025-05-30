package mangopill.customized.common.advancement;

import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import javax.annotation.Nonnull;

public class BasicTrigger extends SimpleCriterionTrigger<BasicTrigger.TriggerInstance> {
    private final ResourceLocation id;

    public BasicTrigger(ResourceLocation id) {
        this.id = id;
    }
    public void trigger(ServerPlayer player) {
        this.trigger(player, TriggerInstance::matches);
    }

    @Override
    protected @Nonnull TriggerInstance createInstance(@Nonnull JsonObject jsonObject, @Nonnull ContextAwarePredicate contextAwarePredicate, @Nonnull DeserializationContext deserializationContext) {
        return new TriggerInstance(id, contextAwarePredicate);
    }

    @Override
    public @Nonnull ResourceLocation getId() {
        return id;
    }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance
    {
        public TriggerInstance(ResourceLocation criterion, ContextAwarePredicate player) {
            super(criterion, player);
        }

        public boolean matches() {
            return true;
        }
    }
}

