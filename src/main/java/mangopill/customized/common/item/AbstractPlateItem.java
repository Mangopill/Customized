package mangopill.customized.common.item;

import mangopill.customized.common.FoodValue;
import mangopill.customized.common.block.AbstractPlateBlock;
import mangopill.customized.common.block.entity.AbstractPlateBlockEntity;
import mangopill.customized.common.block.record.PlateRecord;
import mangopill.customized.common.block.state.PlateState;
import mangopill.customized.common.registry.CAdvancementRegistry;
import mangopill.customized.common.util.CItemStackHandlerHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.*;
import net.minecraft.stats.Stats;
import net.minecraft.world.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.neoforged.neoforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static mangopill.customized.common.util.CItemStackHandlerHelper.*;
import static mangopill.customized.common.util.CStringUtil.*;
import static mangopill.customized.common.util.component.ItemComponentUtil.*;
import static mangopill.customized.common.util.component.ItemComponentUtil.getConsumptionCount;

public abstract class AbstractPlateItem extends BlockItem {
    private final int ingredientInput;
    private final int seasoningInput;
    private final int spiceInput;
    private final boolean canChangeState;

    protected AbstractPlateItem(Supplier<Block> block, Properties properties, int ingredientInput, int seasoningInput, int spiceInput, boolean canChangeState) {
        super(block.get(), properties);
        this.ingredientInput = ingredientInput;
        this.seasoningInput = seasoningInput;
        this.spiceInput = spiceInput;
        this.canChangeState = canChangeState;
    }

    protected <T extends AbstractPlateBlockEntity> AbstractPlateItem(Supplier<Block> block, Properties properties, PlateRecord<T> record) {
        this(block, properties, record.ingredientInput(), record.seasoningInput(), record.spiceInput(), record.canChangeState());
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        tooltipComponents.add(C_ITEM_TEXT.create("consumption_count_total", getConsumptionCountTotal(stack)).withStyle(ChatFormatting.GRAY));
        tooltipComponents.add(C_ITEM_TEXT.create("consumption_count", getConsumptionCount(stack)).withStyle(ChatFormatting.GRAY));
        addItemStackTooltip(stack, tooltipComponents);
        if (getFoodProperty(stack).equals(FoodValue.INEDIBLE)) {
            tooltipComponents.add(C_ITEM_TEXT.create("inedible").withStyle(ChatFormatting.DARK_RED));
        }
        addUuidTooltip(stack, tooltipComponents, context);
        addEffectTooltip(stack, context, tooltipComponents);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack itemstack = player.getItemInHand(usedHand);
        if (level.isClientSide) return InteractionResultHolder.success(itemstack);
        if (getConsumptionCount(itemstack) <= 0) return InteractionResultHolder.pass(itemstack);
        return super.use(level, player, usedHand);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
        if (level.isClientSide) return stack;
        int consumptionCount = getConsumptionCount(stack);
        int consumptionCountTotal = getConsumptionCountTotal(stack);
        FoodProperties properties = getFoodProperty(stack);
        plateAdvancement(livingEntity, properties);
        if (consumptionCount >= 1) {
            level.playSound(null, livingEntity, SoundEvents.GENERIC_EAT, SoundSource.PLAYERS, 0.8F, 0.8F);
            if (livingEntity instanceof ServerPlayer player) {
                player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
                player.getFoodData().eat(properties);
            }
            AbstractPlateBlockEntity.addEffect(livingEntity, properties);
            if (consumptionCount > 1){
                reduceItemStackCountByDivision(getItemStackHandler(stack), getInitialItemStackHandler(stack), consumptionCountTotal);
            } else {
                clearAllSlot(getItemStackHandler(stack));
                clearAllSlot(getInitialItemStackHandler(stack));
                setFoodProperty(stack, FoodValue.EMPTY);
                setConsumptionCountTotal(stack, 0);
            }
            setConsumptionCount(stack, --consumptionCount);
            livingEntity.gameEvent(GameEvent.EAT);
        }
        return stack;
    }

    public static void plateAdvancement(LivingEntity livingEntity, FoodProperties properties) {
        if (!(livingEntity instanceof ServerPlayer serverPlayer)) return;
        if (properties.equals(FoodValue.INEDIBLE)){
            CAdvancementRegistry.EAT_INEDIBLE_STEW.get().trigger(serverPlayer);
        } else {
            CAdvancementRegistry.EAT_NORMAL_STEW.get().trigger(serverPlayer);
        }
    }

    @Override
    public FoodProperties getFoodProperties(ItemStack stack, @Nullable LivingEntity entity) {
        return getFoodProperty(stack);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        Player player = context.getPlayer();
        if (level.isClientSide || player == null) return InteractionResult.SUCCESS;
        return player.isShiftKeyDown() ? super.useOn(context) : use(level, player, context.getHand()).getResult();
    }

    @Override
    public InteractionResult place(BlockPlaceContext context) {
        super.place(context);
        BlockPos pos = context.getClickedPos();
        BlockState state = context.getLevel().getBlockState(pos);
        Level level = context.getLevel();
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (canChangeState) {
            if (blockEntity instanceof AbstractPlateBlockEntity plateBlockEntity && plateBlockEntity.hasInput()){
                level.setBlockAndUpdate(pos, state.setValue(AbstractPlateBlock.DRIVE, PlateState.WITH_DRIVE));
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return getConsumptionCountTotal(stack) > 0;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        int consumptionCount = getConsumptionCount(stack);
        int consumptionCountTotal = getConsumptionCountTotal(stack);
        return (int) Math.ceil((double) consumptionCount / (double) consumptionCountTotal * 13);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return 5592575;
    }

    @Override
    public Component getName(ItemStack stack) {
        List<ItemStack> stackList = getItemStackListInPlate(stack, false);
        List<ItemStack> topTwoItems = getTopTwoItemsByCount(stackList);
        if (topTwoItems.isEmpty()) return super.getName(stack);
        String itemName = topTwoItems.stream().map(item -> item.getDisplayName().getString()).collect(Collectors.joining("&"));
        return literal(itemName).append(":").append(translate(getDescriptionId(stack) + "_food"));
    }

    public void insertItem(ItemStack stack, ItemStackHandler newItemStackHandler) {
        CItemStackHandlerHelper.insertItem(stack, newItemStackHandler, ingredientInput, seasoningInput, spiceInput, 0, null);
    }

    public ItemStackHandler copyItemStackHandlerByComponent(ItemStack stack) {
        return copyItemStackHandler(getItemStackHandler(stack), true);
    }

    public boolean hasInput(ItemStack stack) {
        return CItemStackHandlerHelper.hasInput(getItemStackHandler(stack), getItemStackHandler(stack).getSlots());
    }

    public List<ItemStack> getItemStackListInPlate(ItemStack stack, boolean includeSeasoningAndSpice) {
        return includeSeasoningAndSpice ? CItemStackHandlerHelper.getItemStackListInSlot(getItemStackHandler(stack), 0, getItemStackHandler(stack).getSlots()) :
                CItemStackHandlerHelper.getItemStackListInSlot(getItemStackHandler(stack), 0, ingredientInput);
    }

    public void addItemStackTooltip(ItemStack stack, List<Component> tooltipComponents) {
        List<ItemStack> stackList = getItemStackListInPlate(stack, true);
        if (stackList.isEmpty()) return;
        stackList.forEach(itemStack -> tooltipComponents.add(C_ITEM_TEXT.create("item_stack", itemStack.getCount(), itemStack.getItem().getDescription()).withStyle(ChatFormatting.GRAY)));
    }

    public void addUuidTooltip(ItemStack stack, List<Component> tooltipComponents, TooltipContext context) {
        Level level = context.level();
        if (level == null) return;
        Player player = level.getPlayerByUUID(getLastInteractPlayerId(stack));
        if (player == null) return;
        MutableComponent uuid = C_ITEM_TEXT.create("last_interact_player_id", player.getDisplayName()).withStyle(ChatFormatting.YELLOW);
        if (getAdvancementHasProgress(stack)) {
            uuid.append(C_ITEM_TEXT.create("master_of_culinary_arts")).withStyle(ChatFormatting.GOLD);
        }
        tooltipComponents.add(uuid);
    }

    public void addEffectTooltip(ItemStack stack, TooltipContext context, List<Component> tooltipComponents) {
        if (getFoodProperty(stack).effects().isEmpty()) return;
        getFoodProperty(stack).effects().forEach(buff ->
                tooltipComponents.add(getItemTextBuffComponent(buff, context.tickRate())));
    }

    public int getIngredientInput() {
        return ingredientInput;
    }

    public int getSeasoningInput() {
        return seasoningInput;
    }

    public int getSpiceInput() {
        return spiceInput;
    }

    public boolean isCanChangeState() {
        return canChangeState;
    }
}
