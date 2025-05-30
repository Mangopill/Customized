package mangopill.customized.common.recipe;

import com.google.common.collect.ImmutableSet;
import mangopill.customized.common.registry.ModRecipeRegistry;
import mangopill.customized.common.registry.ModRecipeSerializerRegistry;
import mangopill.customized.common.util.value.PropertyValue;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import javax.annotation.Nonnull;
import java.util.Set;

public record PropertyValueRecipe(Set<ResourceLocation> name, PropertyValue propertyValue, boolean item, ResourceLocation id) implements ModRecipeInterface<Container> {

    public PropertyValueRecipe(Set<ResourceLocation> name, PropertyValue propertyValue, boolean item, ResourceLocation id) {
        this.name = ImmutableSet.copyOf(name);
        this.propertyValue = propertyValue;
        this.item = item;
        this.id = id;
    }

    @Override
    public boolean matches(Container container, @Nonnull Level level) {
        ItemStack stack = container.getItem(0);
        return item ?
                name.stream().anyMatch(name -> name.equals(BuiltInRegistries.ITEM.getKey(stack.getItem()))) :
                name.stream().anyMatch(name -> stack.is(ItemTags.create(name)));
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public @Nonnull ResourceLocation getId() {
        return id;
    }

    @Override
    public @Nonnull ItemStack getResultItem(@Nonnull RegistryAccess registryAccess) {
        return ItemStack.EMPTY;
    }

    @Override
    public @Nonnull ItemStack assemble(@Nonnull Container container, @Nonnull RegistryAccess registryAccess) {
        return ItemStack.EMPTY;
    }

    @Override
    @Nonnull
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializerRegistry.PROPERTY_VALUE.get();
    }

    @Override
    @Nonnull
    public RecipeType<?> getType() {
        return ModRecipeRegistry.PROPERTY_VALUE.get();
    }
}
