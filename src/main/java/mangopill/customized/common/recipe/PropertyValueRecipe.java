package mangopill.customized.common.recipe;

import com.google.common.collect.ImmutableList;
import mangopill.customized.common.recipe.serializer.PropertyValueSerializer;
import mangopill.customized.common.registry.CRecipeRegistry;
import mangopill.customized.common.registry.CRecipeSerializerRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public record PropertyValueRecipe(List<PropertyValueSerializer.PropertyValueGroup> groups) implements CRecipeInterface<RecipeInput> {

    public PropertyValueRecipe(List<PropertyValueSerializer.PropertyValueGroup> groups) {
        this.groups = ImmutableList.copyOf(groups);
    }

    @Override
    public boolean matches(RecipeInput input, Level level) {
        ItemStack stack = input.getItem(0);
        return groups.stream().anyMatch(group -> group.items().stream().anyMatch(name -> name.equals(BuiltInRegistries.ITEM.getKey(stack.getItem())))
                || group.tags().stream().anyMatch(name -> stack.is(ItemTags.create(name))));
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return groups.stream().flatMap(group -> {
                    Stream<Ingredient> itemStream = group.items().stream().map(BuiltInRegistries.ITEM::get).map(Ingredient::of).filter(ingredient -> !ingredient.isEmpty());
                    Stream<Ingredient> tagStream = group.tags().stream().map(ItemTags::create).map(Ingredient::of).filter(ingredient -> !ingredient.isEmpty());
                    return Stream.concat(itemStream, tagStream);
                }).collect(Collectors.toCollection(NonNullList::<Ingredient>create));
    }

    @Override
    public ItemStack assemble(RecipeInput input, HolderLookup.Provider registries) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return ItemStack.EMPTY;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return CRecipeSerializerRegistry.PROPERTY_VALUE.get();
    }

    @Override
    public RecipeType<?> getType() {
        return CRecipeRegistry.PROPERTY_VALUE.get();
    }
}
