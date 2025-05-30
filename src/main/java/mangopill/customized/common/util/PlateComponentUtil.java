package mangopill.customized.common.util;

import com.mojang.datafixers.util.Pair;
import mangopill.customized.common.FoodValue;
import mangopill.customized.common.block.record.PlateSlotRecord;
import net.minecraft.nbt.*;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

public final class PlateComponentUtil {
    private static final String FOOD_KEY = "FoodProperty";
    private static final String ITEM_HANDLER_KEY = "ItemStackHandler";
    private static final String INITIAL_HANDLER_KEY = "InitialItemStackHandler";
    private static final String CONSUMPTION_KEY = "ConsumptionCount";
    private static final String CONSUMPTION_TOTAL_KEY = "ConsumptionCountTotal";

    private PlateComponentUtil() {
    }

    public static FoodProperties getFoodProperty(ItemStack stack) {
        return deserializeFoodProperties(stack.getTag().getCompound(FOOD_KEY));
    }

    public static ItemStackHandler getItemStackHandler(ItemStack stack, @Nullable Integer slot) {
        if (!stack.hasTag()) {
            stack.setTag(new CompoundTag());
        }
        if (stack.getTag().contains("HandlerCache")) {
            CompoundTag cache = stack.getTag().getCompound("HandlerCache");
            if (cache.contains(ITEM_HANDLER_KEY)) {
                CompoundTag handlerTag = cache.getCompound(ITEM_HANDLER_KEY);
                int actualSlots = handlerTag.getList("Items", Tag.TAG_COMPOUND).size();
                ItemStackHandler handler = new ItemStackHandler(actualSlots);
                handler.deserializeNBT(handlerTag);
                return handler;
            }
        }
        if (!stack.getTag().contains(ITEM_HANDLER_KEY)) {
            int defaultSlots = slot == null ? PlateSlotRecord.SOUP_BOWL.ingredientInput() + PlateSlotRecord.SOUP_BOWL.seasoningInput() + 1 : slot;
            ItemStackHandler handler = new ItemStackHandler(defaultSlots);
            stack.getTag().put(ITEM_HANDLER_KEY, handler.serializeNBT());
        }
        CompoundTag handlerTag = stack.getTag().getCompound(ITEM_HANDLER_KEY);
        int actualSlots = handlerTag.getList("Items", Tag.TAG_COMPOUND).size();
        ItemStackHandler handler = new ItemStackHandler(actualSlots);
        handler.deserializeNBT(handlerTag);
        CompoundTag cache = new CompoundTag();
        cache.put(ITEM_HANDLER_KEY, handler.serializeNBT());
        stack.getTag().put("HandlerCache", cache);
        return handler;
    }

    public static ItemStackHandler getInitialItemStackHandler(ItemStack stack) {
        if (!stack.hasTag()) {
            stack.setTag(new CompoundTag());
        }
        if (!stack.getTag().contains(INITIAL_HANDLER_KEY)) {
            CompoundTag mainHandlerTag = stack.getTag().getCompound(ITEM_HANDLER_KEY);
            int defaultSlots = mainHandlerTag.getList("Items", Tag.TAG_COMPOUND).size();
            ItemStackHandler handler = new ItemStackHandler(defaultSlots);
            stack.getTag().put(INITIAL_HANDLER_KEY, handler.serializeNBT());
        }
        CompoundTag handlerTag = stack.getTag().getCompound(INITIAL_HANDLER_KEY);
        int actualSlots = handlerTag.getList("Items", Tag.TAG_COMPOUND).size();
        ItemStackHandler handler = new ItemStackHandler(actualSlots);
        handler.deserializeNBT(handlerTag);
        return handler;
    }

    public static int getConsumptionCount(ItemStack stack) {
        return stack.getTag().getInt(CONSUMPTION_KEY);
    }

    public static int getConsumptionCountTotal(ItemStack stack) {
        return stack.getTag().getInt(CONSUMPTION_TOTAL_KEY);
    }

    public static void setFoodProperty(ItemStack stack, FoodProperties foodProperties) {
        stack.getOrCreateTag().put(FOOD_KEY, serializeFoodProperties(foodProperties));
    }

    public static void setItemStackHandler(ItemStack stack, ItemStackHandler itemStackHandler) {
        stack.getOrCreateTag().put(ITEM_HANDLER_KEY, itemStackHandler.serializeNBT());
        CompoundTag cache = new CompoundTag();
        cache.put(ITEM_HANDLER_KEY, itemStackHandler.serializeNBT());
        stack.getTag().put("HandlerCache", cache);
    }

    public static void setInitialItemStackHandler(ItemStack stack, ItemStackHandler itemStackHandler) {
        stack.getOrCreateTag().put(INITIAL_HANDLER_KEY, itemStackHandler.serializeNBT());
    }

    public static void setConsumptionCount(ItemStack stack, int consumptionCount) {
        stack.getOrCreateTag().putInt(CONSUMPTION_KEY, consumptionCount);
    }

    public static void setConsumptionCountTotal(ItemStack stack, int consumptionCountTotal) {
        stack.getOrCreateTag().putInt(CONSUMPTION_TOTAL_KEY, consumptionCountTotal);
    }

    public static void updateAll(ItemStack stack, ItemStackHandler itemStackHandler, ItemStackHandler initialItemStackHandler, FoodProperties foodProperty, int consumptionCount, int consumptionCountTotal) {
        setItemStackHandler(stack, itemStackHandler);
        setInitialItemStackHandler(stack, initialItemStackHandler);
        setFoodProperty(stack, foodProperty);
        setConsumptionCount(stack, consumptionCount);
        setConsumptionCountTotal(stack, consumptionCountTotal);
    }

    public static CompoundTag serializeFoodProperties(FoodProperties food) {
        CompoundTag tag = new CompoundTag();
        tag.putInt("nutrition", food.getNutrition());
        tag.putFloat("saturation", food.getSaturationModifier());
        tag.putBoolean("isMeat", food.isMeat());
        tag.putBoolean("alwaysEat", food.canAlwaysEat());
        tag.putBoolean("fastFood", food.isFastFood());
        ListTag effects = new ListTag();
        for (Pair<MobEffectInstance, Float> pair : food.getEffects()) {
            CompoundTag effectTag = new CompoundTag();
            effectTag.putInt("id", MobEffect.getId(pair.getFirst().getEffect()));
            effectTag.putInt("duration", pair.getFirst().getDuration());
            effectTag.putInt("amplifier", pair.getFirst().getAmplifier());
            effectTag.putFloat("probability", pair.getSecond());
            effects.add(effectTag);
        }
        tag.put("effects", effects);
        return tag;
    }

    public static FoodProperties deserializeFoodProperties(CompoundTag tag) {
        FoodProperties.Builder builder = new FoodProperties.Builder()
                .nutrition(tag.getInt("nutrition"))
                .saturationMod(tag.getFloat("saturation"));
        if (tag.getBoolean("isMeat")) builder.meat();
        if (tag.getBoolean("alwaysEat")) builder.alwaysEat();
        if (tag.getBoolean("fastFood")) builder.fast();
        if (tag.contains("effects", Tag.TAG_LIST)) {
            ListTag effects = tag.getList("effects", Tag.TAG_COMPOUND);
            for (int i = 0; i < effects.size(); i++) {
                CompoundTag effectTag = effects.getCompound(i);
                MobEffect effect = MobEffect.byId(effectTag.getInt("id"));
                builder.effect(
                        () -> {
                            if (effect != null) {
                                return new MobEffectInstance(
                                        effect,
                                        effectTag.getInt("duration"),
                                        effectTag.getInt("amplifier")
                                );
                            }
                            return null;
                        },
                        effectTag.getFloat("probability")
                );
            }
        }
        return builder.build();
    }
}
