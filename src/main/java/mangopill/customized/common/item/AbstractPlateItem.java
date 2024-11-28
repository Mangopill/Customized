package mangopill.customized.common.item;

import mangopill.customized.Customized;
import mangopill.customized.common.FoodValue;
import mangopill.customized.common.block.record.PlateRecord;
import mangopill.customized.common.tag.ModTag;
import mangopill.customized.common.util.ModItemStackHandlerHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.StringUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.gameevent.GameEvent;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

import static mangopill.customized.common.util.ModItemStackHandlerHelper.*;
import static mangopill.customized.common.util.PlateComponentUtil.getConsumptionCount;
import static mangopill.customized.common.util.PlateComponentUtil.*;

public abstract class AbstractPlateItem extends BlockItem {

    public AbstractPlateItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, Item.@NotNull TooltipContext context, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        tooltipComponents.add(Component.translatable("item_text." + Customized.MODID + ".consumption_count_total", getConsumptionCountTotal(stack)).withStyle(ChatFormatting.GRAY));
        tooltipComponents.add(Component.translatable("item_text." + Customized.MODID + ".consumption_count", getConsumptionCount(stack)).withStyle(ChatFormatting.GRAY));
        List<ItemStack> stackList = getItemStackListInPlate(stack, true);
        if (!stackList.isEmpty()) {
            stackList.forEach(itemStack -> {
                tooltipComponents.add(Component.translatable("item_text." + Customized.MODID + ".item_stack",
                        itemStack.getCount(),
                        Component.translatable(itemStack.getItem().getDescriptionId())).withStyle(ChatFormatting.GRAY));
            });
        }
        if (getFoodProperty(stack).equals(FoodValue.INEDIBLE)) {
            tooltipComponents.add(Component.translatable("item_text." + Customized.MODID + ".inedible").withStyle(ChatFormatting.DARK_RED));
        }
        if (!getFoodProperty(stack).effects().isEmpty()) {
            getFoodProperty(stack).effects().forEach(buff -> {
                int i = Mth.floor((float) buff.effectSupplier().get().getDuration());
                Component component = Component.literal(StringUtil.formatTickDuration(i, context.tickRate()));
                tooltipComponents.add(Component.translatable("item_text." + Customized.MODID + ".buff",
                                Component.translatable(buff.effectSupplier().get().getEffect().value().getDescriptionId()), component)
                        .withStyle(buff.effectSupplier().get().getEffect().value().getCategory().getTooltipFormatting()));
            });
        }
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand usedHand) {
        ItemStack itemstack = player.getItemInHand(usedHand);
        if (level.isClientSide) {
            return InteractionResultHolder.success(itemstack);
        }
        if (getConsumptionCount(itemstack) <= 0){
            return InteractionResultHolder.pass(itemstack);
        }
        return super.use(level, player, usedHand);
    }

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity livingEntity) {
        if (level.isClientSide) {
            return stack;
        }
        int consumptionCount = getConsumptionCount(stack);
        int consumptionCountTotal = getConsumptionCountTotal(stack);
        ItemStackHandler newItemStackHandler = copyItemStackHandlerByComponent(stack);
        if(consumptionCount >= 1) {
            level.playSound(null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), livingEntity.getEatingSound(stack), SoundSource.NEUTRAL, 1.0F, 1.0F + (level.random.nextFloat() - level.random.nextFloat()) * 0.4F);
            if (!livingEntity.level().isClientSide()) {
                for (FoodProperties.PossibleEffect foodproperties$possibleeffect : getFoodProperty(stack).effects()) {
                    if (livingEntity.getRandom().nextFloat() < foodproperties$possibleeffect.probability()) {
                        livingEntity.addEffect(foodproperties$possibleeffect.effect());
                    }
                }
                if (getFoodProperty(stack).effects().equals(FoodValue.INEDIBLE.effects())) {
                    livingEntity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 500, 1));
                }
            }
            if (consumptionCount > 1){
                reduceItemStackCountByDivision(newItemStackHandler, consumptionCountTotal);
            } else {
                clearAllSlot(newItemStackHandler);
                setFoodProperty(stack, FoodValue.NULL);
                setConsumptionCountTotal(stack, 0);
            }
            setItemStackHandler(stack, newItemStackHandler);
            setConsumptionCount(stack, --consumptionCount);
            livingEntity.gameEvent(GameEvent.EAT);
        }
        return stack;
    }

    @Override
    public FoodProperties getFoodProperties(@NotNull ItemStack stack, @Nullable LivingEntity entity) {
        return getFoodProperty(stack);
    }

    public void insertItem(ItemStack itemStackInHand, ItemStackHandler itemStackHandler) {
        if (itemStackInHand.is(ModTag.SEASONING)) {
            fillInItem(itemStackHandler, itemStackInHand, getIngredientInput(), getIngredientInput() + getSeasoningInput());
            return;
        }
        if (itemStackInHand.is(ModTag.FAMOUS_SPICE)) {
            fillInItem(itemStackHandler, itemStackInHand, getIngredientInput() + getSeasoningInput(), itemStackHandler.getSlots());
            return;
        }
        fillInItem(itemStackHandler, itemStackInHand, 0, getIngredientInput());
    }

    public ItemStackHandler copyItemStackHandlerByComponent(ItemStack stack){
        ItemStackHandler newItemStackHandler = new ItemStackHandler(getItemStackHandler(stack).getSlots());
        getItemStackListInPlate(stack, true).forEach(itemStack -> insertItem(itemStack.copy(), newItemStackHandler));
        return newItemStackHandler;
    }

    public boolean hasInput(ItemStack stack) {
        return ModItemStackHandlerHelper.hasInput(getItemStackHandler(stack), getItemStackHandler(stack).getSlots());
    }

    //getItemStackList
    public List<ItemStack> getItemStackListInPlate(ItemStack stack, boolean includeSeasoningAndSpice) {
        return includeSeasoningAndSpice ? ModItemStackHandlerHelper.getItemStackListInSlot(getItemStackHandler(stack), 0, getItemStackHandler(stack).getSlots()) :
                ModItemStackHandlerHelper.getItemStackListInSlot(getItemStackHandler(stack), 0, getIngredientInput()) ;
    }

    abstract public int getIngredientInput();

    abstract public int getSeasoningInput();
}
