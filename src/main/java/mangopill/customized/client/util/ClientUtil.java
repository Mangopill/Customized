package mangopill.customized.client.util;

import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import mangopill.customized.client.util.strategy.*;
import mangopill.customized.common.block.entity.*;
import mangopill.customized.common.item.AbstractPlateItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.core.Direction;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.*;

import java.util.*;
import java.util.Random;

import static mangopill.customized.common.util.StringUtil.*;

@OnlyIn(Dist.CLIENT)
public final class ClientUtil {
    private ClientUtil() {
    }

    public static void registerPlateItemProperty(Item item) {
        ItemProperties.register(item, getCLoc("drive"), (stack, level, player, seed) ->
                (stack.getItem() instanceof AbstractPlateItem plate && plate.hasInput(stack)) ? 1.0F : 0.0F);
    }

    public static void renderBlockEntityByStrategy(BlockEntity blockEntity, PoseStack poseStack, MultiBufferSource buffer,
                                 int light, int overlay, IRenderStrategy... strategies) {
        renderBlockEntityByStrategy(blockEntity, poseStack, buffer, light, overlay, Arrays.asList(strategies));
    }

    public static void renderBlockEntityByStrategy(BlockEntity blockEntity, PoseStack poseStack, MultiBufferSource buffer,
                                                   int light, int overlay, Collection<IRenderStrategy> strategies) {
        Level level = blockEntity.getLevel();
        if (level == null) return;
        for (IRenderStrategy iRenderStrategy : strategies) {
            iRenderStrategy.render(poseStack, buffer, light, overlay);
        }
    }

    public static void renderItemStackByStrategy(PoseStack poseStack, MultiBufferSource buffer,
                                                 int light, int overlay, IRenderStrategy... strategies) {
        renderItemStackByStrategy(poseStack, buffer, light, overlay, Arrays.asList(strategies));
    }

    public static void renderItemStackByStrategy(PoseStack poseStack, MultiBufferSource buffer,
                                            int light, int overlay, Collection<IRenderStrategy> strategies) {
        if (Minecraft.getInstance().player == null) return;
        for (IRenderStrategy iRenderStrategy : strategies) {
            iRenderStrategy.render(poseStack, buffer, light, overlay);
        }
    }

    // Specific rendering implementation for brewing barrels.
    public static <T extends CBasicCookingBlockEntity<?>> void renderBrewingBarrel(T blockEntity, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay,
                                                                                   float startX, float startY, float startZ, float scaleX, float scaleY, float scaleZ, float degreesX, float degreesY, float degreesZ) {
        Level level = blockEntity.getLevel();
        if (level == null) return;
        List<ItemStack> stackList = blockEntity.getItemStackListInBlockEntity(false);
        BlockState state = level.getBlockState(blockEntity.getBlockPos());
        List<IRenderStrategy> strategies = new ArrayList<>();
        for (int i = 0; i < stackList.size(); i++) {
            ItemStack itemStack = stackList.get(i);
            int col = i % 2;
            int row = i / 2;
            float x = startX + col * startX;
            float y = startY + row * startY;
            strategies.add(new RenderSimpleAdjustItemStack(itemStack, state, x, y,
                    startZ, scaleX, scaleY, scaleZ, degreesX, degreesY, degreesZ));
        }
        ItemStack renderItemStack = blockEntity.getItemStackHandler().getStackInSlot(blockEntity.getInputSlot());
        strategies.add(new RenderSimpleAdjustItemStack(renderItemStack, state, 3 * startX, startY, startZ,
                scaleX, scaleY, scaleZ, degreesX, degreesY, degreesZ));
        renderBlockEntityByStrategy(blockEntity, poseStack, buffer, light, overlay, strategies);
    }

    public static void adjust(PoseStack poseStack, @Nullable BlockState state, float x, float y, float z) {
        Direction direction = Direction.SOUTH;
        boolean isFacing = true;
        if (state != null) {
            direction = state.hasProperty(BlockStateProperties.FACING)
                    ? state.getValue(BlockStateProperties.FACING)
                    : state.hasProperty(BlockStateProperties.HORIZONTAL_FACING)
                    ? state.getValue(BlockStateProperties.HORIZONTAL_FACING)
                    : Direction.NORTH;
            isFacing = state.hasProperty(BlockStateProperties.FACING);
        }
        applyRotation(poseStack, direction, x, y, z, isFacing);
    }

    public static void applyRotation(PoseStack poseStack, Direction facing, float x, float y, float z, boolean isFacing) {
        float origZ = z;
        float origY = y;
        switch (facing) {
            case NORTH -> z = 1.0F - z;
            case SOUTH -> x = 1.0F - x;
            case WEST -> {z = 1.0F - x; x = 1.0F - origZ;}
            case EAST -> {z = x; x = origZ;}
            case UP -> {y = origZ; z = 1.0F - origY; }
            case DOWN -> {y = 1.0F - origZ; z = origY; }
        }
        poseStack.translate(x, y, z);
        float yRot = (facing.toYRot() + (isFacing ? 180 : 0)) % 360;
        poseStack.mulPose(Axis.YP.rotationDegrees(-yRot));
        switch (facing) {
            case UP -> poseStack.mulPose(Axis.XP.rotationDegrees(-90.0F));
            case DOWN -> poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
        }
    }

    public static void renderFluidLayer(VertexConsumer buffer, PoseStack poseStack, float fluidLevel,
                                        float minX, float minZ, float maxX, float maxZ,
                                        int color, int light, int overlay, TextureAtlasSprite texture) {
        float u0 = texture.getU0();
        float u1 = texture.getU1();
        float v0 = texture.getV0();
        float v1 = texture.getV1();
        poseStack.pushPose();
        PoseStack.Pose pose = poseStack.last();
        vertex(buffer, pose, minX, fluidLevel, minZ, color, u0, v0, overlay, light);
        vertex(buffer, pose, minX, fluidLevel, maxZ, color, u0, v1, overlay, light);
        vertex(buffer, pose, maxX, fluidLevel, maxZ, color, u1, v1, overlay, light);
        vertex(buffer, pose, maxX, fluidLevel, minZ, color, u1, v0, overlay, light);
        poseStack.popPose();
    }

    public static void vertex(VertexConsumer buffer, PoseStack.Pose pose, float x, float y, float z, int color, float u, float v, int overlay, int light) {
        buffer.addVertex(pose, x, y, z).setColor(color).setUv(u, v).setOverlay(overlay).setLight(light).setNormal(pose, 0.0F, 1.0F, 0.0F);
    }

    public static RandomXYZ getRandomXYZ(List<ItemStack> stackList, float startLength, float startWidth, float startHeight, float endLength, float endWidth, float endHeight, ItemStack stack, int i, ItemStack newStack) {
        Random rand = new Random(stackList.lastIndexOf(stack) + i);
        int seed = Item.getId(newStack.getItem()) + rand.nextInt() + i;
        Random random = new Random(seed);
        float randX = startLength + (random.nextFloat(endLength - startLength));
        float randY = startHeight + (random.nextFloat(endHeight - startHeight));
        float randZ = startWidth + (random.nextFloat(endWidth - startWidth));
        return new RandomXYZ(seed, random, randX, randY, randZ);
    }

    public record RandomXYZ(int seed, Random random, float randX, float randY, float randZ) {}
}