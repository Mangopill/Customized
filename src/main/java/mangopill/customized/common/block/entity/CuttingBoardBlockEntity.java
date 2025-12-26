package mangopill.customized.common.block.entity;

import mangopill.customized.common.block.handler.CuttingBoardItemHandler;
import mangopill.customized.common.recipe.CuttingBoardRecipe;
import mangopill.customized.common.registry.*;
import net.minecraft.core.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;

import java.util.Optional;

import static mangopill.customized.common.util.CItemStackHandlerHelper.*;
import static mangopill.customized.common.util.CStringUtil.*;
import static mangopill.customized.common.util.InteractUtil.*;

public class CuttingBoardBlockEntity extends CBasicCookingBlockEntity<CuttingBoardRecipe> {
    private final IItemHandler inputAndOutputHandler;
    private int times;
    private int totalTimes;

    public CuttingBoardBlockEntity(BlockPos pos, BlockState blockState) {
        super(CBlockEntityTypeRegistry.CUTTING_BOARD.get(), pos, blockState, 1, 0, RecipeManager.createCheck(CRecipeRegistry.CUTTING_BOARD.get()));
        this.inputAndOutputHandler = new CuttingBoardItemHandler(this, itemStackHandler);
    }

    @Override
    public int setSlotLimit() {
        return 1;
    }

    @Override
    protected void cookRecipe(Level level, CuttingBoardRecipe recipe, BlockPos pos, BlockState state) {}

    @Override
    public void interact(ItemStack itemStackInHand, Player player, Level level, InteractionHand hand, BlockState state, BlockPos pos, SoundEvent output, SoundEvent insert) {
        RecipeWrapper wrapper = new RecipeWrapper(itemStackHandler);
        Optional<CuttingBoardRecipe> matchRecipe = getMatchRecipe(wrapper);
        if (!itemStackHandler.getStackInSlot(0).isEmpty() && !itemStackInHand.isEmpty()) {
            if (matchRecipe.isEmpty() || matchRecipe.get().toolItem().stream().noneMatch(ingredient -> ingredient.test(itemStackInHand))) {
                player.displayClientMessage(C_MESSAGE.create("cutting_board"), true);
                return;
            }
            if (times <= 0) {
                Holder<Enchantment> sharpnessHolder = player.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.SHARPNESS);
                int baseCuttingTimes = matchRecipe.get().cuttingTimes();
                times = Math.max(0, baseCuttingTimes + itemStackInHand.getEnchantmentLevel(sharpnessHolder) / 2);
                totalTimes = times;
            }
            if (times == 1) {
                itemStackHandler.getStackInSlot(0).shrink(1);
                totalTimes = 0;
            }
            spawnItemEntityList(level, matchRecipe.get().output().stream().map(ItemStack::copy).toList(), state, pos);
            spawnItemEntityList(level, matchRecipe.get().probabilityOutput().stream()
                    .filter(itemStack -> level.random.nextFloat() < itemStack.probability())
                    .flatMap(itemStack -> itemStack.probabilityStackList().stream())
                    .map(ItemStack::copy).toList(), state, pos);
            hurtAndBreakItemStack(itemStackInHand, player, 1);
            times--;
            itemStackHandlerChanged();
            return;
        }
        super.interact(itemStackInHand, player, level, hand, state, pos, output, insert);
    }

    @Override
    public void outputItem(Level level, BlockState state, BlockPos pos) {
        if (times <= 0) {
            super.outputItem(level, state, pos);
        }
    }

    @Override
    public void loadAdditional(CompoundTag compound, HolderLookup.Provider registries) {
        super.loadAdditional(compound, registries);
        times = compound.getInt("Times");
        totalTimes = compound.getInt("TotalTimes");
    }

    @Override
    public void saveAdditional(CompoundTag compound, HolderLookup.Provider registries) {
        super.saveAdditional(compound, registries);
        compound.putInt("Times", times);
        compound.putInt("TotalTimes", totalTimes);
    }

    public IItemHandler getInputAndOutputHandler() {
        return inputAndOutputHandler;
    }

    public int getTimes() {
        return times;
    }

    public int getTotalTimes() {
        return totalTimes;
    }
}
