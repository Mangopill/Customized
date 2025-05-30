package mangopill.customized.common.util;

import com.google.gson.JsonArray;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.stream.Collectors;

public final class JsonUtil {
    private JsonUtil() {
    }
    public static NonNullList<Ingredient> jsonArrayToNonNullList(JsonArray jsonArray) {
        return jsonArray.asList()
                .stream()
                .map(Ingredient::fromJson)
                .collect(Collectors.toCollection(NonNullList::create));
    }
}
