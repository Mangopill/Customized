package mangopill.customized.common.recipe;

import com.google.common.collect.ImmutableSet;
import mangopill.customized.common.registry.ModRecipeRegistry;
import mangopill.customized.common.registry.ModRecipeSerializerRegistry;
import mangopill.customized.common.util.value.PropertyValue;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class PropertyValueRecipe implements ModRecipeInterface<RecipeInput>{
    private final Set<ResourceLocation> name;
    private final PropertyValue propertyValue;
    private final boolean item;

    public PropertyValueRecipe(Set<ResourceLocation> name, PropertyValue propertyValue, boolean item) {
        this.name = ImmutableSet.copyOf(name);
        this.propertyValue = propertyValue;
        this.item = item;
    }

    @Override
    public boolean matches(RecipeInput input, @NotNull Level level) {
        ItemStack stack = input.getItem(0);
        return item ?
                name.stream().anyMatch(name -> name.equals(BuiltInRegistries.ITEM.getKey(stack.getItem()))) :
                name.stream().anyMatch(name -> stack.is(ItemTags.create(name)));
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public @NotNull ItemStack getResultItem(HolderLookup.@NotNull Provider registries) {
        return ItemStack.EMPTY;
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull RecipeInput input, HolderLookup.@NotNull Provider registries) {
        return ItemStack.EMPTY;
    }

    public Set<ResourceLocation> getName() {
        return name;
    }

    public PropertyValue getPropertyValue() {
        return propertyValue;
    }

    @NotNull
    public static PropertyValue getPropertyValue(ItemStack stack, Level level) {
        List<RecipeHolder<PropertyValueRecipe>> recipeHolder = level.getRecipeManager().getRecipesFor(ModRecipeRegistry.PROPERTY_VALUE.get(), new SingleRecipeInput(stack), level);
        if (recipeHolder.isEmpty()) {
            return new PropertyValue();
        }
        return recipeHolder.stream()
                .map(RecipeHolder::value)
                .filter(PropertyValueRecipe::isItem)
                .findFirst().map(PropertyValueRecipe::getPropertyValue)
                .orElseGet(() -> {
                    PropertyValue propertyValue = new PropertyValue();
                    long maxCount = 0L;
                    HashMap<ResourceLocation, PropertyValue> map = new HashMap<>();
                    recipeHolder.stream().map(RecipeHolder::value).forEach(valueRecipe ->
                            valueRecipe.getName().forEach(name -> map.put(name, valueRecipe.getPropertyValue()))
                    );
                    for (ResourceLocation tag : stack.getTags().map(TagKey::location).filter(map::containsKey).toList()) {
                        long count = tag.getPath().chars().filter(c -> c == '/').count();
                        if (count >= maxCount) {
                            if (count > maxCount) {
                                maxCount = count;
                                propertyValue.replace();
                            }
                            map.get(tag).toSet().forEach(entry ->
                                    propertyValue.put(entry.getKey(),
                                            Math.max(propertyValue.getBigger(entry.getKey()), entry.getValue()))
                            );
                        }
                    }
                    return propertyValue;
                });
    }

    public boolean isItem() {
        return item;
    }

    @Override
    @NotNull
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializerRegistry.PROPERTY_VALUE.get();
    }

    @Override
    @NotNull
    public RecipeType<?> getType() {
        return ModRecipeRegistry.PROPERTY_VALUE.get();
    }

}
