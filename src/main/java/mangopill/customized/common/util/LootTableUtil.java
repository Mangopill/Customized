package mangopill.customized.common.util;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.*;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

public final class LootTableUtil {
    private LootTableUtil() {}

    public static ItemStack getRandomLootTableItemStack(ServerLevel level, ResourceKey<LootTable> resourceKey) {
        LootParams.Builder builder = new LootParams.Builder(level);
        LootParams params = builder.create(LootContextParamSets.EMPTY);
        LootTable lootTable = level.getServer().reloadableRegistries().getLootTable(resourceKey);
        ObjectArrayList<ItemStack> objectArrayList = lootTable.getRandomItems(params);
        if (objectArrayList.isEmpty()) return ItemStack.EMPTY;
        return objectArrayList.get(level.random.nextInt(objectArrayList.size())).copy();
    }
}
