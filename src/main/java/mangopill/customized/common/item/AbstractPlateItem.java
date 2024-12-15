package mangopill.customized.common.item;

import mangopill.customized.Customized;
import mangopill.customized.common.FoodValue;
import mangopill.customized.common.block.AbstractPlateBlock;
import mangopill.customized.common.block.AbstractPotBlock;
import mangopill.customized.common.block.entity.AbstractPlateBlockEntity;
import mangopill.customized.common.block.entity.AbstractPotBlockEntity;
import mangopill.customized.common.block.state.PlateState;
import mangopill.customized.common.block.state.PotState;
import mangopill.customized.common.registry.ModAdvancementRegistry;
import mangopill.customized.common.util.ModItemStackHandlerHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.StringUtil;
import net.minecraft.world.*;
import net.minecraft.world.effect.*;
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
import java.util.*;

import static mangopill.customized.common.util.ModItemStackHandlerHelper.*;
import static mangopill.customized.common.util.PlateComponentUtil.*;
import static mangopill.customized.common.util.PropertyValueUtil.*;

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
        FoodProperties properties = getFoodProperty(stack);
        plateAdvancement(livingEntity, properties);
        if(consumptionCount >= 1) {
            level.playSound(null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), livingEntity.getEatingSound(stack),
                    SoundSource.NEUTRAL, 1.0F, 1.0F + (level.random.nextFloat() - level.random.nextFloat()) * 0.4F);
            AbstractPlateBlockEntity.addEffect(livingEntity, properties);
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

    public static void plateAdvancement(@NotNull LivingEntity livingEntity, FoodProperties properties) {
        if (livingEntity instanceof ServerPlayer serverPlayer) {
            if (properties.equals(FoodValue.INEDIBLE)){
                ModAdvancementRegistry.EAT_INEDIBLE_STEW.get().trigger(serverPlayer);
            } else {
                ModAdvancementRegistry.EAT_NORMAL_STEW.get().trigger(serverPlayer);
            }
        }
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
        AbstractPotBlockEntity potEntity = getPotEntity(level, pos);
        BlockState state = level.getBlockState(pos);
        if (level.isClientSide || player == null) {
            return InteractionResult.SUCCESS;
        }
        if (blockEntity != null && blockEntity.equals(potEntity) && player.isShiftKeyDown()) {
            if (!state.getValue(AbstractPotBlock.LID).equals(PotState.WITH_DRIVE) || !potEntity.isHeated()){
                return InteractionResult.PASS;
            }
            return getInteractionResult(getPotEntity(level, pos), itemInHand, level, player);
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

    @Override
    public @NotNull Component getName(@NotNull ItemStack stack) {
        List<ItemStack> stackList = getItemStackListInPlate(stack, false);
        List<ItemStack> topTwoItems = getTopTwoItemsByCount(stackList);
        if (topTwoItems.size() == 1) {
            ItemStack aStack = getTopTwoItemsByCount(stackList).getFirst();
            return Component.translatable("").append(aStack.getDisplayName())
                    .append(":").append(Component.translatable(this.getDescriptionId(stack) + "_food"));
        }
        if (topTwoItems.size() == 2) {
            ItemStack aStack = getTopTwoItemsByCount(stackList).getFirst();
            ItemStack bStack = getTopTwoItemsByCount(stackList).get(1);
            return Component.translatable("").append(aStack.getDisplayName()).append("&").append(bStack.getDisplayName())
                    .append(":").append(Component.translatable(this.getDescriptionId(stack) + "_food"));
        }
        return Component.translatable(this.getDescriptionId(stack));
    }

    private InteractionResult getInteractionResult(AbstractPotBlockEntity potBlockEntity, ItemStack itemInHand, Level level, Player player) {
        List<ItemStack> stackList = potBlockEntity.getItemStackListInPot(false, true);
        ItemStackHandler newItemStackHandler = copyItemStackHandlerByComponent(itemInHand);
        spawnUsingConvertsTo(player, stackList);
        stackList.forEach(itemStack -> insertItem(itemStack, newItemStackHandler));
        List<ItemStack> newStackList = getItemStackListInSlot(newItemStackHandler, 0, newItemStackHandler.getSlots());
        potBlockEntity.itemStackHandlerChanged();
        updateAll(itemInHand, newItemStackHandler, getFoodPropertyByPropertyValue(level, newStackList, true), getConsumptionCount(newStackList), getConsumptionCount(newStackList));
        return InteractionResult.SUCCESS;
    }

    public void insertItem(ItemStack stack, ItemStackHandler newItemStackHandler) {
        ModItemStackHandlerHelper.insertItem(stack, newItemStackHandler, ingredientInput, seasoningInput, 1);
    }

    public ItemStackHandler copyItemStackHandlerByComponent(ItemStack stack){
        ItemStackHandler newItemStackHandler = new ItemStackHandler(getItemStackHandler(stack).getSlots());
        getItemStackListInPlate(stack, true).forEach(itemStack -> insertItem(itemStack, newItemStackHandler));
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
            stackList.forEach(itemStack ->
                    tooltipComponents.add(Component.translatable("item_text." + Customized.MODID + ".item_stack",
                        itemStack.getCount(),
                        Component.translatable(itemStack.getItem().getDescriptionId())).withStyle(ChatFormatting.GRAY)));
        }
    }

    public void addEffectTooltip(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltipComponents) {
        if (!getFoodProperty(stack).effects().isEmpty()) {
            getFoodProperty(stack).effects().forEach(buff -> {
                int i = Mth.floor((float) buff.effectSupplier().get().getDuration());
                Component component = Component.literal(StringUtil.formatTickDuration(i, context.tickRate()));
                MobEffect mobEffect = buff.effectSupplier().get().getEffect().value();
                tooltipComponents.add(Component.translatable("item_text." + Customized.MODID + ".buff",
                                Component.translatable(mobEffect.getDescriptionId())
                                        .append(Component.translatable("enchantment.level." + (buff.effect().getAmplifier() + 1)))
                                , component)
                        .withStyle(mobEffect.getCategory().getTooltipFormatting()));
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
