package mangopill.customized.common.block.entity;

import mangopill.customized.common.block.CrateBlock;
import mangopill.customized.common.block.handler.CrateItemHandler;
import mangopill.customized.common.recipe.CrateRecipe;
import mangopill.customized.common.registry.*;
import mangopill.customized.common.util.record.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;

import java.util.List;

import static mangopill.customized.common.util.CItemStackHandlerHelper.*;

public class CrateBlockEntity extends CBasicCookingBlockEntity<CrateRecipe> {
    private final IItemHandler inputAndOutputHandler;

    public CrateBlockEntity(BlockPos pos, BlockState blockState) {
        super(CBlockEntityTypeRegistry.CRATE.get(), pos, blockState, 18, 0, RecipeManager.createCheck(CRecipeRegistry.CRATE.get()));
        this.inputAndOutputHandler = new CrateItemHandler(this, itemStackHandler);
    }

    @Override
    public void cookingTick(Level level, BlockPos pos, BlockState state, RecipeWrapper wrapper) {
        if (!isSunny(pos)){
            reduceCookingTime();
            return;
        }
        super.cookingTick(level, pos, state, wrapper);
    }

    protected void cookRecipe(Level level, RecipeHolder<CrateRecipe> holder, BlockPos pos, BlockState state) {
        ++cookingTime;
        getRecipeCookingCompletionTime(holder);
        if (cookingTime < cookingCompletionTime) {
            return;
        }
        Block block = level.getBlockState(pos).getBlock();
        if (block instanceof CrateBlock) {
            ItemStack resultStack = holder.value().getResultItem(this.level.registryAccess()).copy();
            spawnItemEntity(level, resultStack.copy(), state, pos);
            for (int i = 0; i < allSlot; ++i) {
                ItemStack slotStack = itemStackHandler.getStackInSlot(i);
                spawnUsingConvertsTo(level, List.of(slotStack), state, pos);
                if (!slotStack.isEmpty()){
                    slotStack.shrink(1);
                }
            }
            cookingTime = 0;
        }
    }

    @Override
    public void insertItem(ItemStack itemStackInHand) {
        if (!hasInput() || ItemStack.isSameItemSameComponents(getItemStackListInBlockEntity(true).getFirst(), itemStackInHand)) {
            super.insertItem(itemStackInHand);
        }
    }

    protected void reduceCookingTime() {
        if (cookingTime > 0){
            cookingTime--;
        }
        itemStackHandlerChanged();
    }

    public void getRecipeCookingCompletionTime(RecipeHolder<CrateRecipe> holder){
        cookingCompletionTime = holder.value().getCookingTime();
    }

    public boolean isSunny(BlockPos pos) {
        return level != null
                && !level.isNight()
                && level.canSeeSky(pos.above())
                && !level.isRainingAt(pos.above());
    }

    public ItemStack getCloneItemStack(ItemStack stack) {
        ItemStack itemStack = new ItemStack(stack.getItem());
        itemStack.applyComponents(collectComponents());
        return itemStack;
    }

    @Override
    public void removeComponentsFromTag(CompoundTag tag) {
        tag.remove("ItemStackHandler");
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder component) {
        super.collectImplicitComponents(component);
        component.set(CDataComponentRegistry.ITEM_STACK_HANDLER, new ItemStackHandlerRecord(itemStackHandler));
    }

    @Override
    protected void applyImplicitComponents(BlockEntity.DataComponentInput componentInput) {
        super.applyImplicitComponents(componentInput);
        ItemStackHandler componentHandler = componentInput
                .getOrDefault(CDataComponentRegistry.ITEM_STACK_HANDLER, ItemStackHandlerRecord.NULL)
                .itemStackHandler();
        List<ItemStack> stacks = getItemStackListInSlot(componentHandler, 0, componentHandler.getSlots());
        stacks.forEach(stack ->
                insertItem(stack.copy())
        );
    }

    public IItemHandler getInputAndOutputHandler() {
        return inputAndOutputHandler;
    }
}
