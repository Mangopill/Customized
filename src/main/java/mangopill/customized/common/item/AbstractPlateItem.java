package mangopill.customized.common.item;

import mangopill.customized.Customized;
import mangopill.customized.client.event.renderer.SoupBowlItemRenderer;
import mangopill.customized.common.FoodValue;
import mangopill.customized.common.block.AbstractPlateBlock;
import mangopill.customized.common.block.AbstractPotBlock;
import mangopill.customized.common.block.entity.AbstractPlateBlockEntity;
import mangopill.customized.common.block.entity.AbstractPotBlockEntity;
import mangopill.customized.common.block.state.PlateState;
import mangopill.customized.common.block.state.PotState;
import mangopill.customized.common.registry.ModAdvancementRegistry;
import mangopill.customized.common.util.CreateItemStackHandler;
import mangopill.customized.common.util.ModItemStackHandlerHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
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
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;

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
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new SoupBowlItemRenderer.SoupBowlItemExtensions());
    }

    @Override
    public void appendHoverText(@Nonnull ItemStack stack, @Nullable Level level, @Nonnull List<Component> tooltipComponents, @Nonnull TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, level, tooltipComponents, tooltipFlag);
        tooltipComponents.add(Component.translatable("item_text." + Customized.MODID + ".consumption_count_total", getConsumptionCountTotal(stack)).withStyle(ChatFormatting.GRAY));
        tooltipComponents.add(Component.translatable("item_text." + Customized.MODID + ".consumption_count", getConsumptionCount(stack)).withStyle(ChatFormatting.GRAY));
        addItemStackTooltip(stack, tooltipComponents);
        if (hasInput(stack) && !getFoodProperty(stack).canAlwaysEat()) {
            tooltipComponents.add(Component.translatable("item_text." + Customized.MODID + ".inedible").withStyle(ChatFormatting.DARK_RED));
        }
        addEffectTooltip(stack, level, tooltipComponents);
    }

    @Override
    public @Nonnull InteractionResultHolder<ItemStack> use(@Nonnull Level level, @Nonnull Player player, @Nonnull InteractionHand usedHand) {
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
    public @Nonnull ItemStack finishUsingItem(@Nonnull ItemStack stack, @Nonnull Level level, @Nonnull LivingEntity livingEntity) {
        if (level.isClientSide) {
            return stack;
        }
        ItemStackHandler handler = getItemStackHandler(stack, null);
        ItemStackHandler initialHandler = getInitialItemStackHandler(stack);

        int consumptionCount = getConsumptionCount(stack);
        int consumptionCountTotal = getConsumptionCountTotal(stack);
        FoodProperties properties = getFoodProperty(stack);
        plateAdvancement(livingEntity, properties);
        if(consumptionCount >= 1) {
            level.playSound(null, livingEntity, SoundEvents.GENERIC_EAT, SoundSource.PLAYERS, 0.8F, 0.8F);
            if (livingEntity instanceof ServerPlayer player) {
                player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
                player.getFoodData().eat(properties.getNutrition(), properties.getSaturationModifier());
            }
            AbstractPlateBlockEntity.addEffect(livingEntity, properties);
            if (consumptionCount > 1){
                reduceItemStackCountByDivision(handler, initialHandler, consumptionCountTotal);
            } else {
                clearAllSlot(handler);
                clearAllSlot(initialHandler);
                setFoodProperty(stack, FoodValue.NULL);
                setConsumptionCountTotal(stack, 0);
            }
            setConsumptionCount(stack, --consumptionCount);
            setItemStackHandler(stack, handler);
            livingEntity.gameEvent(GameEvent.EAT);
        }
        return stack;
    }

    public static void plateAdvancement(@Nonnull LivingEntity livingEntity, FoodProperties properties) {
        if (livingEntity instanceof ServerPlayer serverPlayer) {
            if (properties.equals(FoodValue.INEDIBLE)){
                ModAdvancementRegistry.EAT_INEDIBLE_STEW.trigger(serverPlayer);
            } else {
                ModAdvancementRegistry.EAT_NORMAL_STEW.trigger(serverPlayer);
            }
        }
    }

    @Override
    public FoodProperties getFoodProperties(@Nonnull ItemStack stack, @Nullable LivingEntity entity) {
        return getFoodProperty(stack);
    }

    @Override
    public @Nonnull InteractionResult useOn(@Nonnull UseOnContext context) {
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
        return player.isShiftKeyDown() ? super.useOn(context) : use(level, player, context.getHand()).getResult();
    }

    @Override
    public @Nonnull InteractionResult place(@Nonnull BlockPlaceContext context) {
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
    public boolean isBarVisible(@Nonnull ItemStack stack) {
        return getConsumptionCountTotal(stack) > 0;
    }

    @Override
    public int getBarWidth(@Nonnull ItemStack stack) {
        int consumptionCount = getConsumptionCount(stack);
        int consumptionCountTotal = getConsumptionCountTotal(stack);
        return (int) Math.ceil((double) consumptionCount / consumptionCountTotal * 13);
    }

    @Override
    public int getBarColor(@Nonnull ItemStack stack) {
        return 5592575;
    }

    @Override
    public @Nonnull Component getName(@Nonnull ItemStack stack) {
        List<ItemStack> stackList = getItemStackListInPlate(stack, false);
        List<ItemStack> topTwoItems = getTopTwoItemsByCount(stackList);
        if (topTwoItems.size() == 1) {
            ItemStack aStack = getTopTwoItemsByCount(stackList).get(0);
            return Component.translatable("").append(aStack.getDisplayName())
                    .append(":").append(Component.translatable(this.getDescriptionId(stack) + "_food"));
        }
        if (topTwoItems.size() == 2) {
            ItemStack aStack = getTopTwoItemsByCount(stackList).get(0);
            ItemStack bStack = getTopTwoItemsByCount(stackList).get(1);
            return Component.translatable("").append(aStack.getDisplayName()).append("&").append(bStack.getDisplayName())
                    .append(":").append(Component.translatable(this.getDescriptionId(stack) + "_food"));
        }
        return Component.translatable(this.getDescriptionId(stack));
    }

    @Override
    protected boolean updateCustomBlockEntityTag(@Nonnull BlockPos pos, Level level, @Nullable Player player,
                                                 @Nonnull ItemStack stack, @Nonnull BlockState state) {
        if (level.getBlockEntity(pos) instanceof AbstractPlateBlockEntity plateEntity) {
            CompoundTag tag = stack.getOrCreateTag();
            plateEntity.load(tag);
            plateEntity.setChanged();
            return true;
        }
        return false;
    }

    private InteractionResult getInteractionResult(AbstractPotBlockEntity potBlockEntity, ItemStack itemInHand, Level level, Player player) {
        List<ItemStack> stackList = potBlockEntity.getItemStackListInPot(false, true);
        ItemStackHandler newItemStackHandler = copyItemStackHandlerByComponent(itemInHand);
        spawnUsingConvertsTo(player, stackList);
        stackList.forEach(itemStack -> insertItem(itemStack, newItemStackHandler));
        List<ItemStack> newStackList = getItemStackListInSlot(newItemStackHandler, 0, newItemStackHandler.getSlots());
        ItemStackHandler initialItemStackHandler = new ItemStackHandler(newItemStackHandler.getSlots());
        newStackList.forEach(itemStack -> insertItem(itemStack.copy(), initialItemStackHandler));
        potBlockEntity.itemStackHandlerChanged();
        updateAll(itemInHand, newItemStackHandler, initialItemStackHandler, getFoodPropertyByPropertyValue(level, newStackList, true), getConsumptionCount(newStackList), getConsumptionCount(newStackList));
        return InteractionResult.SUCCESS;
    }

    public void insertItem(ItemStack stack, ItemStackHandler newItemStackHandler) {
        ModItemStackHandlerHelper.insertItem(stack, newItemStackHandler, ingredientInput, seasoningInput, 1);
    }

    public ItemStackHandler copyItemStackHandlerByComponent(ItemStack stack){
        ItemStackHandler newItemStackHandler = new ItemStackHandler(getItemStackHandler(stack, null).getSlots());
        getItemStackListInPlate(stack, true).forEach(itemStack -> insertItem(itemStack.copy(), newItemStackHandler));
        return newItemStackHandler;
    }

    public boolean hasInput(ItemStack stack) {
        return ModItemStackHandlerHelper.hasInput(getItemStackHandler(stack, null), getItemStackHandler(stack, null).getSlots());
    }

    public List<ItemStack> getItemStackListInPlate(ItemStack stack, boolean includeSeasoningAndSpice) {
        return includeSeasoningAndSpice ? ModItemStackHandlerHelper.getItemStackListInSlot(getItemStackHandler(stack, null), 0, getItemStackHandler(stack, null).getSlots()) :
                ModItemStackHandlerHelper.getItemStackListInSlot(getItemStackHandler(stack, null), 0, ingredientInput);
    }

    public void addItemStackTooltip(ItemStack stack, @Nonnull List<Component> tooltipComponents) {
        List<ItemStack> stackList = getItemStackListInPlate(stack, true);
        if (!stackList.isEmpty()) {
            stackList.forEach(itemStack ->
                    tooltipComponents.add(Component.translatable("item_text." + Customized.MODID + ".item_stack",
                        itemStack.getCount(),
                        Component.translatable(itemStack.getItem().getDescriptionId())).withStyle(ChatFormatting.GRAY)));
        }
    }

    public void addEffectTooltip(@Nonnull ItemStack stack, @Nullable Level level, @Nonnull List<Component> tooltipComponents) {
        if (!getFoodProperty(stack).getEffects().isEmpty()) {
            getFoodProperty(stack).getEffects().forEach(buff -> {
                int i = Mth.floor((float) buff.getFirst().getDuration());
                Component component = Component.literal(StringUtil.formatTickDuration(i));
                MobEffect mobEffect = buff.getFirst().getEffect();
                tooltipComponents.add(Component.translatable("item_text." + Customized.MODID + ".buff",
                                Component.translatable(mobEffect.getDescriptionId())
                                        .append(Component.translatable("enchantment.level." + (buff.getFirst().getAmplifier() + 1)))
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
