package mangopill.customized.common.item;

import mangopill.customized.Customized;
import mangopill.customized.common.FoodValue;
import mangopill.customized.common.block.AbstractPlateBlock;
import mangopill.customized.common.block.AbstractPotBlock;
import mangopill.customized.common.block.entity.AbstractPlateBlockEntity;
import mangopill.customized.common.block.entity.AbstractPotBlockEntity;
import mangopill.customized.common.block.state.PlateState;
import mangopill.customized.common.block.state.PotState;
import mangopill.customized.common.tag.ModTag;
import mangopill.customized.common.util.ModItemStackHandlerHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.StringUtil;
import net.minecraft.world.*;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

import static mangopill.customized.common.util.ModItemStackHandlerHelper.*;
import static mangopill.customized.common.util.PlateComponentUtil.*;

public abstract class AbstractPlateItem extends BlockItem {
    private final int ingredientInput;
    private final int seasoningInput;
    private final boolean canInputDrive;

    public AbstractPlateItem(Block block, Properties properties, int ingredientInput, int seasoningInput, boolean canInputDrive) {
        super(block, properties);
        this.ingredientInput = ingredientInput;
        this.seasoningInput = seasoningInput;
        this.canInputDrive = canInputDrive;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, Item.@NotNull TooltipContext context, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        tooltipComponents.add(Component.translatable("item_text." + Customized.MODID + ".consumption_count_total", getConsumptionCountTotal(stack)).withStyle(ChatFormatting.GRAY));
        tooltipComponents.add(Component.translatable("item_text." + Customized.MODID + ".consumption_count", getConsumptionCount(stack)).withStyle(ChatFormatting.GRAY));
        addItemStackTooltip(stack, tooltipComponents);
        if (getFoodProperty(stack).equals(FoodValue.INEDIBLE)) {
            tooltipComponents.add(Component.translatable("item_text." + Customized.MODID + ".inedible").withStyle(ChatFormatting.DARK_RED));
        }
        addEffectTooltip(stack, context, tooltipComponents);
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
            level.playSound(null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), livingEntity.getEatingSound(stack),
                    SoundSource.NEUTRAL, 1.0F, 1.0F + (level.random.nextFloat() - level.random.nextFloat()) * 0.4F);
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

    @Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext context) {
        Level level = context.getLevel();
        ItemStack itemInHand = context.getItemInHand();
        Player player = context.getPlayer();
        BlockPos pos = context.getClickedPos();
        BlockEntity blockEntity = level.getBlockEntity(pos);
        BlockEntity potEntity = getPotEntity(level, pos);
        BlockState state = level.getBlockState(pos);
        if (level.isClientSide || player == null) {
            return InteractionResult.SUCCESS;
        }
        if (blockEntity != null && blockEntity.equals(potEntity) && player.isShiftKeyDown()) {
            if (!state.getValue(AbstractPotBlock.LID).equals(PotState.WITH_DRIVE)){
                return InteractionResult.PASS;
            }
            return getInteractionResult(getPotEntity(level, pos), itemInHand, level);
        }
        return player.isShiftKeyDown() ? super.useOn(context) : InteractionResult.PASS;
    }

    @Override
    public @NotNull InteractionResult place(@NotNull BlockPlaceContext context) {
        super.place(context);
        BlockPos pos = context.getClickedPos();
        BlockState state = context.getLevel().getBlockState(pos);
        Level level = context.getLevel();
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (canInputDrive) {
            if (blockEntity instanceof AbstractPlateBlockEntity plateBlockEntity && plateBlockEntity.hasInput()){
                level.setBlockAndUpdate(pos, state.setValue(AbstractPlateBlock.DRIVE, PlateState.WITH_DRIVE));
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public boolean isBarVisible(@NotNull ItemStack stack) {
        return getConsumptionCountTotal(stack) > 0;
    }

    @Override
    public int getBarWidth(@NotNull ItemStack stack) {
        int consumptionCount = getConsumptionCount(stack);
        int consumptionCountTotal = getConsumptionCountTotal(stack);
        return (int) Math.ceil((double) consumptionCount / consumptionCountTotal * 13);
    }

    @Override
    public int getBarColor(@NotNull ItemStack stack) {
        return 5592575;
    }

    private InteractionResult getInteractionResult(AbstractPotBlockEntity potBlockEntity, ItemStack itemInHand, Level level) {
        List<ItemStack> stackList = potBlockEntity.getItemStackListInPot(false, true);
        ItemStackHandler newItemStackHandler = copyItemStackHandlerByComponent(itemInHand);
        stackList.forEach(stack -> insertItem(stack, newItemStackHandler));
        List<ItemStack> newStackList = getItemStackListInSlot(newItemStackHandler, 0, newItemStackHandler.getSlots());
        potBlockEntity.itemStackHandlerChanged();
        updateAll(itemInHand, newItemStackHandler, getFoodPropertyByPropertyValue(level, newStackList, true), getConsumptionCount(newStackList), getConsumptionCount(newStackList));
        return InteractionResult.SUCCESS;
    }

    public void insertItem(ItemStack itemStackInHand, ItemStackHandler itemStackHandler) {
        if (itemStackInHand.is(ModTag.SEASONING)) {
            fillInItem(itemStackHandler, itemStackInHand, ingredientInput, ingredientInput + seasoningInput);
            return;
        }
        if (itemStackInHand.is(ModTag.FAMOUS_SPICE)) {
            fillInItem(itemStackHandler, itemStackInHand, ingredientInput + seasoningInput, itemStackHandler.getSlots());
            return;
        }
        fillInItem(itemStackHandler, itemStackInHand, 0, ingredientInput);
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
                ModItemStackHandlerHelper.getItemStackListInSlot(getItemStackHandler(stack), 0, ingredientInput) ;
    }

    public void addItemStackTooltip(@NotNull ItemStack stack, @NotNull List<Component> tooltipComponents) {
        List<ItemStack> stackList = getItemStackListInPlate(stack, true);
        if (!stackList.isEmpty()) {
            stackList.forEach(itemStack -> {
                tooltipComponents.add(Component.translatable("item_text." + Customized.MODID + ".item_stack",
                        itemStack.getCount(),
                        Component.translatable(itemStack.getItem().getDescriptionId())).withStyle(ChatFormatting.GRAY));
            });
        }
    }

    public void addEffectTooltip(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltipComponents) {
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

    abstract public AbstractPotBlockEntity getPotEntity(Level level, BlockPos pos);

    public int getIngredientInput() {
        return ingredientInput;
    }

    public int getSeasoningInput() {
        return seasoningInput;
    }

    public boolean isCanInputDrive() {
        return canInputDrive;
    }
}
