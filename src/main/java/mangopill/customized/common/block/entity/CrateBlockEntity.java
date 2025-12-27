package mangopill.customized.common.block.entity;

import mangopill.customized.common.block.CrateBlock;
import mangopill.customized.common.block.handler.CrateItemHandler;
import mangopill.customized.common.recipe.CrateRecipe;
import mangopill.customized.common.registry.*;
import mangopill.customized.common.util.record.ItemStackHandlerRecord;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.*;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;

import java.util.List;

import static mangopill.customized.common.util.CItemStackHandlerHelper.*;
import static mangopill.customized.common.util.component.CItemMatchMode.*;

public class CrateBlockEntity extends CBasicCookingBlockEntity<CrateRecipe> {
    private final IItemHandler inputAndOutputHandler;

    public CrateBlockEntity(BlockPos pos, BlockState blockState) {
        super(CBlockEntityTypeRegistry.CRATE.get(), pos, blockState, 18, 0, RecipeManager.createCheck(CRecipeRegistry.CRATE.get()));
        this.inputAndOutputHandler = new CrateItemHandler(this, itemStackHandler);
    }

    @Override
    protected boolean canCookRecipe(CrateRecipe recipe, RecipeWrapper wrapper) {
        if (recipe.sunny() && !isSunny()){
            reduceCookingTime();
            return false;
        }
        return super.canCookRecipe(recipe, wrapper);
    }

    protected void cookRecipe(Level level, CrateRecipe recipe, BlockPos pos, BlockState state) {
        ++cookingTime;
        getRecipeCookingCompletionTime(recipe);
        if (cookingTime < cookingCompletionTime) return;
        Block block = level.getBlockState(pos).getBlock();
        if (!(block instanceof CrateBlock)) return;
        ItemStack resultStack = recipe.getResultItem(level.registryAccess()).copy();
        spawnItemEntity(level, resultStack.copy(), state, pos);
        for (int i = 0; i < recipe.ingredientCount(); ++i) {
            ItemStack stack = getTemplateItem().copyWithCount(recipe.ingredientCount());
            spawnUsingConvertsTo(level, List.of(stack), state, pos);
        }
        shrinkMatchingItem(itemStackHandler, null, recipe.ingredientCount());
        cookingTime = 0;
    }

    @Override
    public void insertItem(ItemStack itemStackInHand) {
        if (!hasInput() || simpleTest(getTemplateItem(), itemStackInHand, SAME_ITEM_SAME_COMPONENTS)) {
            super.insertItem(itemStackInHand);
        }
    }

    public ItemStack getTemplateItem() {
        List<ItemStack> stackList = getItemStackListInBlockEntity(false);
        return stackList.isEmpty() ? ItemStack.EMPTY : stackList.getFirst();
    }

    protected void reduceCookingTime() {
        if (cookingTime > 0){
            cookingTime--;
        }
        itemStackHandlerChanged();
    }

    public void getRecipeCookingCompletionTime(CrateRecipe recipe){
        cookingCompletionTime = recipe.cookingTime();
    }

    public boolean isSunny() {
        return level != null
                && !level.isNight()
                && level.canSeeSky(this.getBlockPos().above())
                && !level.isRainingAt(this.getBlockPos().above());
    }

    public ItemStack getCloneItemStack(ItemStack stack) {
        ItemStack itemStack = new ItemStack(stack.getItem());
        itemStack.applyComponents(collectComponents());
        return itemStack;
    }

    @Override
    public void removeComponentsFromTag(CompoundTag tag) {
        tag.remove("ItemStackHandler");
        tag.remove("CookingTime");
        tag.remove("CookingCompletionTime");
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
                .getOrDefault(CDataComponentRegistry.ITEM_STACK_HANDLER, ItemStackHandlerRecord.EMPTY)
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
