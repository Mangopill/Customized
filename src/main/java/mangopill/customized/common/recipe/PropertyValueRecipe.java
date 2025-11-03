package mangopill.customized.common.recipe;

import com.google.common.collect.ImmutableSet;
import mangopill.customized.common.registry.CRecipeRegistry;
import mangopill.customized.common.registry.CRecipeSerializerRegistry;
import mangopill.customized.common.util.value.PropertyValue;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import java.util.Set;

public record PropertyValueRecipe(Set<ResourceLocation> name, PropertyValue propertyValue, boolean item) implements CRecipeInterface<RecipeInput> {

    public PropertyValueRecipe(Set<ResourceLocation> name, PropertyValue propertyValue, boolean item) {
        this.name = ImmutableSet.copyOf(name);
        this.propertyValue = propertyValue;
        this.item = item;
    }

    @Override
    public boolean matches(RecipeInput input, Level level) {
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
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack assemble(RecipeInput input, HolderLookup.Provider registries) {
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
