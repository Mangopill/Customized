package mangopill.customized.common.block.entity;

import mangopill.customized.common.block.handler.CuttingBoardItemHandler;
import mangopill.customized.common.recipe.*;
import mangopill.customized.common.registry.CBlockEntityTypeRegistry;
import mangopill.customized.common.registry.CRecipeRegistry;
import net.minecraft.core.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;

import java.util.Optional;

import static mangopill.customized.common.util.CItemStackHandlerHelper.*;

public class CuttingBoardBlockEntity extends CBasicCookingBlockEntity<CuttingBoardRecipe> {
    private final IItemHandler inputAndOutputHandler;
    private int times;
    private int totalTimes;

    public CuttingBoardBlockEntity(BlockPos pos, BlockState blockState) {
        super(CBlockEntityTypeRegistry.CUTTING_BOARD.get(), pos, blockState, 1, 0, RecipeManager.createCheck(CRecipeRegistry.CUTTING_BOARD.get()));
        this.inputAndOutputHandler = new CuttingBoardItemHandler(this, itemStackHandler);
    }

    @Override
    protected void cookRecipe(Level level, RecipeHolder<CuttingBoardRecipe> holder, BlockPos pos, BlockState state) {}

    @Override
    public void interact(ItemStack itemStackInHand, Player player, Level level, InteractionHand hand, BlockState state, BlockPos pos, SoundEvent output, SoundEvent insert) {
        super.interact(itemStackInHand, player, level, hand, state, pos, output, insert);
        RecipeWrapper wrapper = new RecipeWrapper(itemStackHandler);
        Optional<RecipeHolder<CuttingBoardRecipe>> matchRecipe = getMatchRecipe(wrapper);
        if (matchRecipe.isEmpty() || matchRecipe.get().value().toolItem().stream().noneMatch(ingredient -> ingredient.test(itemStackInHand))) {
            return;
        }
        if (times <= 0) {
            Holder<Enchantment> sharpnessHolder = player.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.SHARPNESS);
            int baseCuttingTimes = matchRecipe.get().value().cuttingTimes();
            times = Math.max(0, baseCuttingTimes + itemStackInHand.getEnchantmentLevel(sharpnessHolder) / 2);
            totalTimes = times;
        }
        if (times == 1) {
            itemStackHandler.getStackInSlot(0).shrink(1);
            totalTimes = 0;
        }
        matchRecipe.get().value().output().forEach(itemStack -> spawnItemEntity(level, itemStack.copy(), state, pos));
        matchRecipe.get().value().probabilityOutput().forEach(itemStack -> {
            if (level.random.nextFloat() < matchRecipe.get().value().probability()) {
                spawnItemEntity(level, itemStack.copy(), state, pos);
            }
        });
        itemStackInHand.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
        times--;
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

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag, registries);
        return tag;
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
