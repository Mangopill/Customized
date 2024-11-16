package mangopill.customized.common.block.entity;

import mangopill.customized.Customized;
import mangopill.customized.common.block.handler.PotItemHandler;
import mangopill.customized.common.tag.ModTag;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Nameable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public abstract class AbstractPotBlockEntity extends BlockEntity implements Nameable {
    private static int ingredientInput;
    private static int seasoningInput;
    private static final int SPICE_INPUT = 1;
    private static final int OUTPUT = 1;
    public static final int ALL_SLOT = ingredientInput + seasoningInput + SPICE_INPUT + OUTPUT;
    private final ItemStackHandler itemStackHandler;
    private final IItemHandler inputHandler;
    private final IItemHandler outputHandler;
    private final String potName;
    private int cookingTime;
    private int cookingCompletionTime;

    public AbstractPotBlockEntity(BlockEntityType<? extends AbstractPotBlockEntity> type, BlockPos pos, BlockState blockState, int ingredientCount, int seasoningCount, String potName) {
        super(type, pos, blockState);
        ingredientInput = ingredientCount;
        seasoningInput = seasoningCount;
        this.itemStackHandler = createItemStackHandler();
        this.inputHandler = new PotItemHandler(itemStackHandler, Direction.UP, ingredientCount, seasoningCount);
        this.outputHandler = new PotItemHandler(itemStackHandler, Direction.DOWN, ingredientCount, seasoningCount);
        this.potName = potName;
    }

    protected ItemStackHandler createItemStackHandler() {
        return new ItemStackHandler(ALL_SLOT)
        {
            @Override
            protected void onContentsChanged(int slot) {
                itemStackHandlerChanged();
            }
        };
    }

    public int getCustomizedCookingCompletionTime(){
        cookingCompletionTime = 100;
        for (int i = 0; i < ingredientInput; ++i) {
            cookingCompletionTime += itemStackHandler.getStackInSlot(i).getCount() * 5;
        }
        return cookingCompletionTime;
    }

    public void stirFryAccelerate(ItemStack itemStackInhand){
        if (itemStackInhand.is(ModTag.SPATULA)){
            cookingTime += 10;
        }
    }
/*    public void addItemToPot(ItemStack itemStackInHand) {
        if (itemStackInHand.is(ModTag.SEASONING)){
            addToArray(itemStackInHand, 4, seasoning);
        }else if (itemStackInHand.is(ModTag.SPICE)){
            addToArray(itemStackInHand, 1, spice);
        }
    }

    public void addToArray(ItemStack itemStackInHand, Integer maxCount, ItemStack[] stack){
        for (int i = 0; i < stack.length; i++) {
            if (stack[i] == null) {
                stack[i] = itemStackInHand.split(maxCount);
                return;
            }
            if (stack[i].getItem() == itemStackInHand.getItem()) {
                int newCount = stack[i].getCount() + itemStackInHand.getCount();
                if (newCount <= maxCount) {
                    stack[i].setCount(newCount);
                    itemStackInHand.shrink(itemStackInHand.getCount());
                } else {
                    stack[i].setCount(maxCount);
                    itemStackInHand.shrink(maxCount - stack[i].getCount());
                }
                return;
            }
        }
    }*/

    @Override
    @Nullable
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.@NotNull Provider registries) {
        return saveWithoutMetadata(registries);
    }

    @Override
    public @NotNull Component getName() {
        return Component.translatable("pot." + Customized.MODID + "." + potName);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return getName();
    }

    @Override
    @Nullable
    public Component getCustomName() {
        return getName();
    }

    protected void itemStackHandlerChanged() {
        super.setChanged();
        if (level != null){
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
        }
    }

    public boolean isHeated(Level level, BlockPos pos) {
        BlockState stateBelow = level.getBlockState(pos.below());
        if (!stateBelow.is(ModTag.HEAT_SOURCE)) {
            return false;
        }
        if (stateBelow.hasProperty(BlockStateProperties.LIT)){
            return stateBelow.getValue(BlockStateProperties.LIT);
        }
        return true;
    }

    public static int getIngredientInput() {
        return ingredientInput;
    }

    public static int getSeasoningInput() {
        return seasoningInput;
    }

    public ItemStackHandler getItemStackHandler() {
        return itemStackHandler;
    }

    public IItemHandler getInputHandler() {
        return inputHandler;
    }

    public IItemHandler getOutputHandler() {
        return outputHandler;
    }

    public String getPotName() {
        return potName;
    }
}
