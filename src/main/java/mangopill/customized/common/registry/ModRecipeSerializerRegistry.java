package mangopill.customized.common.registry;

import mangopill.customized.Customized;
import mangopill.customized.common.recipe.serializer.CasseroleSerializer;
import mangopill.customized.common.recipe.serializer.PropertyValueSerializer;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class  ModRecipeSerializerRegistry {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZER = DeferredRegister.create(Registries.RECIPE_SERIALIZER, Customized.MODID);

    public static final Supplier<RecipeSerializer<?>> PROPERTY_VALUE = RECIPE_SERIALIZER.register("property_value", PropertyValueSerializer::new);
    public static final Supplier<RecipeSerializer<?>> CASSEROLE = RECIPE_SERIALIZER.register("casserole", CasseroleSerializer::new);
}
