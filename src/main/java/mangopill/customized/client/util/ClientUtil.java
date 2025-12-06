package mangopill.customized.client.util;

import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import mangopill.customized.client.event.model.VariableFluidBakedModel;
import mangopill.customized.common.block.AbstractPotBlock;
import mangopill.customized.common.block.entity.*;
import mangopill.customized.common.block.handler.PotFluidHandler;
import mangopill.customized.common.block.state.PotState;
import mangopill.customized.common.item.AbstractPlateItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.ClientHooks;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.client.textures.FluidSpriteCache;
import org.jetbrains.annotations.*;

import java.util.*;

@OnlyIn(Dist.CLIENT)
public final class ClientUtil {
    private ClientUtil() {
    }

    public static void renderDrivePot(AbstractPotBlockEntity potBlockEntity, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay,
                                      float startLength, float startWidth, float startHeight, float endLength, float endWidth, float endHeight,
                                      ResourceLocation fluidModel, float fluidHeight) {
        List<ItemStack> stackList = potBlockEntity.getItemStackListInPot(false, false);
        boolean dynamic = potBlockEntity.getBlockState().getValue(AbstractPotBlock.LID).equals(PotState.WITH_DRIVE);
        Level level = potBlockEntity.getLevel();
        if (level == null) {
            return;
        }
        renderFluid(level, potBlockEntity.getBlockState(), potBlockEntity.getBlockPos(), potBlockEntity.getFluidHandler(), poseStack, buffer, light, overlay, fluidModel, fluidHeight);
        renderItemListDrive(level, stackList, poseStack, buffer, light, overlay, startLength, startWidth, startHeight, endLength, endWidth, endHeight, RenderType.cutout(), dynamic);
    }

    public static void renderLayeredPot(AbstractPotBlockEntity potBlockEntity, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay,
                                      float startLength, float startWidth, float startHeight, float endLength, float endWidth, float endHeight) {
        List<ItemStack> stackList = potBlockEntity.getItemStackListInPot(false, false);
        renderItemStackLayered(stackList, poseStack, buffer, light, overlay, startLength, startWidth, startHeight, endLength, endWidth, endHeight);
    }

    public static void renderDrivePlate(AbstractPlateBlockEntity plateBlockEntity, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay,
                                        float startLength, float startWidth, float startHeight, float endLength, float endWidth, float endHeight) {
        List<ItemStack> stackList = plateBlockEntity.getItemStackListInPlate(false);
        Level level = plateBlockEntity.getLevel();
        if (level == null) {
            return;
        }
        renderItemListDrive(level, stackList, poseStack, buffer, light, overlay, startLength, startWidth, startHeight, endLength, endWidth, endHeight, RenderType.cutout(), true);
    }

    public static void renderLayeredPlate(AbstractPlateBlockEntity plateBlockEntity, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay,
                                        float startLength, float startWidth, float startHeight, float endLength, float endWidth, float endHeight) {
        List<ItemStack> stackList = plateBlockEntity.getItemStackListInPlate(false);
        Level level = plateBlockEntity.getLevel();
        if (level == null) {
            return;
        }
        renderItemStackLayered(stackList, poseStack, buffer, light, overlay, startLength, startWidth, startHeight, endLength, endWidth, endHeight);
    }

    public static void renderDrivePlateItem(ItemStack stack, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay,
                                            float startLength, float startWidth, float startHeight, float endLength, float endWidth, float endHeight, ResourceLocation model) {
        if (stack.getItem() instanceof AbstractPlateItem plateItem){
            List<ItemStack> stackList = plateItem.getItemStackListInPlate(stack, false);
            if (Minecraft.getInstance().player == null) {
                return;
            }
            renderModel(stack, ItemDisplayContext.NONE, false, poseStack, buffer, light, overlay, model);
            renderItemListDrive(Minecraft.getInstance().player.level(), stackList, poseStack, buffer, light, overlay, startLength, startWidth, startHeight, endLength, endWidth, endHeight, RenderType.cutout(), true);
        }
    }

    public static void renderLayeredPlateItem(ItemStack stack, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay,
                                            float startLength, float startWidth, float startHeight, float endLength, float endWidth, float endHeight, ResourceLocation model) {
        if (stack.getItem() instanceof AbstractPlateItem plateItem){
            List<ItemStack> stackList = plateItem.getItemStackListInPlate(stack, false);
            if (Minecraft.getInstance().player == null) {
                return;
            }
            renderModel(stack, ItemDisplayContext.NONE, false, poseStack, buffer, light, overlay, model);
            renderItemStackLayered(stackList, poseStack, buffer, light, overlay, startLength, startWidth, startHeight, endLength, endWidth, endHeight);
        }
    }

    public static void renderItemListDrive(Level level, List<ItemStack> stackList, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay,
                                           float startLength, float startWidth, float startHeight, float endLength, float endWidth, float endHeight,
                                           RenderType renderType, boolean dynamic) {
        float globalTime = level.getGameTime() % 24000 * 0.05F;
        for (ItemStack stack : stackList) {
            ItemStack newStack = stack.copy();
            if (newStack.isEmpty()) {
                continue;
            }
            int count = newStack.getCount();
            int renderCount = count / 4 + (count % 4 > 0 ? 1 : 0);
            for (int i = 0; i < renderCount; i++) {
                RandomXYZ randomXYZ = getRandomXYZ(stackList, startLength, startWidth, startHeight, endLength, endWidth, endHeight, stack, i, newStack);
                float phase = (randomXYZ.seed % 1000) * 0.1F;
                float deltaY = (float) Math.sin(globalTime * 0.8F + phase) * 0.03F;
                float animatedY = dynamic ? randomXYZ.randY + deltaY : randomXYZ.randY;
                poseStack.pushPose();
                poseStack.translate(randomXYZ.randX, animatedY, randomXYZ.randZ);
                poseStack.mulPose(Axis.ZP.rotationDegrees(randomXYZ.random.nextFloat(360.0F)));
                poseStack.mulPose(Axis.XP.rotationDegrees(randomXYZ.random.nextFloat(360.0F)));
                poseStack.mulPose(Axis.YP.rotationDegrees(randomXYZ.random.nextFloat(360.0F)));
                poseStack.scale(0.3F, 0.3F, 0.3F);
                ItemStack renderItemStack = newStack.copy();
                renderItemStack.setCount(1);
                BakedModel model = Minecraft.getInstance().getItemRenderer().getModel(renderItemStack, null, null, 0);
                model = ClientHooks.handleCameraTransforms(poseStack, model, ItemDisplayContext.FIXED, false);
                poseStack.translate(-0.5F, -0.5F, -0.5F);
                Minecraft.getInstance().getItemRenderer().renderModelLists(model, renderItemStack, light, overlay, poseStack, buffer.getBuffer(renderType));
                poseStack.popPose();
            }
        }
    }

    public static void renderItemStackLayered(List<ItemStack> stackList, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay,
                                              float startLength, float startWidth, float startHeight, float endLength, float endWidth, float endHeight) {
        for (ItemStack stack : stackList) {
            ItemStack newStack = stack.copy();
            if (newStack.isEmpty()) {
                continue;
            }
            int count = newStack.getCount();
            int renderCount = count / 4 + (count % 4 > 0 ? 1 : 0);
            for (int i = 0; i < renderCount; i++) {
                RandomXYZ randomXYZ = getRandomXYZ(stackList, startLength, startWidth, startHeight, endLength, endWidth, endHeight, stack, i, newStack);
                poseStack.pushPose();
                poseStack.translate(randomXYZ.randX, randomXYZ.randY, randomXYZ.randZ);
                poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
                poseStack.mulPose(Axis.ZP.rotationDegrees(randomXYZ.random.nextFloat(360.0F)));
                poseStack.scale(0.2F, 0.2F, 0.2F);
                ItemStack renderItemStack = newStack.copy();
                renderItemStack.setCount(1);
                Minecraft.getInstance().getItemRenderer().renderStatic(renderItemStack, ItemDisplayContext.FIXED, light, overlay, poseStack, buffer, null, 0);
                poseStack.popPose();
            }
        }
    }

    private static RandomXYZ getRandomXYZ(List<ItemStack> stackList, float startLength, float startWidth, float startHeight, float endLength, float endWidth, float endHeight, ItemStack stack, int i, ItemStack newStack) {
        Random rand = new Random(stackList.lastIndexOf(stack) + i);
        int seed = Item.getId(newStack.getItem()) + rand.nextInt() + i;
        Random random = new Random(seed);
        float randX = startLength + (random.nextFloat(endLength - startLength));
        float randY = startHeight + (random.nextFloat(endHeight - startHeight));
        float randZ = startWidth + (random.nextFloat(endWidth - startWidth));
        return new RandomXYZ(seed, random, randX, randY, randZ);
    }

    private record RandomXYZ(int seed, Random random, float randX, float randY, float randZ) { }

    public static <T extends CBasicCookingBlockEntity<?>> void renderBrewingBarrel(T blockEntity, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay,
                                                                                   float startX, float startY, float startZ, float scaleX, float scaleY, float scaleZ, float degreesX, float degreesY, float degreesZ) {
        Level level = blockEntity.getLevel();
        if (level == null) {
            return;
        }
        List<ItemStack> stackList = blockEntity.getItemStackListInBlockEntity(false);
        BlockState state = blockEntity.getLevel().getBlockState(blockEntity.getBlockPos());
        for (int i = 0; i < stackList.size(); i++) {
            ItemStack itemStack = stackList.get(i);
            int col = i % 2;
            int row = i / 2;
            float x = startX + col * startX;
            float y = startY + row * startY;
            renderItemStack(itemStack, poseStack, buffer, state, light, overlay, x, y, startZ, scaleX, scaleY, scaleZ, degreesX, degreesY, degreesZ);
        }
        ItemStack renderItemStack = blockEntity.getItemStackHandler().getStackInSlot(blockEntity.getInputSlot());
        renderItemStack(renderItemStack, poseStack, buffer, state, light, overlay, 3 * startX, startY, startZ, scaleX, scaleY, scaleZ, degreesX, degreesY, degreesZ);
    }

    public static void renderBasicBlock(CBasicCookingBlockEntity<?> blockEntity, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay,
                                        float startX, float startY, float startZ, float scaleX, float scaleY, float scaleZ, float degreesX, float degreesY, float degreesZ) {
        Level level = blockEntity.getLevel();
        if (level == null) {
            return;
        }
        List<ItemStack> stackList = blockEntity.getItemStackListInBlockEntity(false);
        BlockState state = blockEntity.getLevel().getBlockState(blockEntity.getBlockPos());
        for (ItemStack stack : stackList) {
            renderItemStack(stack, poseStack, buffer, state, light, overlay, startX, startY, startZ, scaleX, scaleY, scaleZ, degreesX, degreesY, degreesZ);
        }
    }

    public static void renderItemStack(ItemStack itemStack, PoseStack poseStack, MultiBufferSource buffer, @Nullable BlockState state, int light,
                                       int overlay, float startX, float startY, float startZ, float scaleX, float scaleY,
                                       float scaleZ, float degreesX, float degreesY, float degreesZ) {
        poseStack.pushPose();
        adjust(poseStack, state, startX, startY, startZ);
        poseStack.scale(scaleX, scaleY, scaleZ);
        poseStack.mulPose(Axis.XP.rotationDegrees(degreesX));
        poseStack.mulPose(Axis.YP.rotationDegrees(degreesY));
        poseStack.mulPose(Axis.ZP.rotationDegrees(degreesZ));
        if (!itemStack.isEmpty()) {
            ItemStack renderItemStack = itemStack.copy();
            renderItemStack.setCount(1);
            Minecraft.getInstance().getItemRenderer().renderStatic(renderItemStack, ItemDisplayContext.FIXED, light, overlay, poseStack, buffer, null, 0);
        }
        poseStack.popPose();
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

    public static <T extends LivingEntity, M extends EntityModel<T>> void renderHatModel(PoseStack poseStack, MultiBufferSource buffer, int packedLight, M model, ItemStack stack, float scale, double translateY, ResourceLocation texture) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        poseStack.pushPose();
        if (model instanceof HumanoidModel<?> humanoidModel) {
            humanoidModel.head.translateAndRotate(poseStack);
        }
        poseStack.scale(scale, scale, scale);
        poseStack.translate(0.0D, translateY, 0.0D);
        poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
        itemRenderer.render(stack, ItemDisplayContext.FIXED, false, poseStack, buffer, packedLight, OverlayTexture.NO_OVERLAY, itemRenderer.getItemModelShaper().getModelManager().getModel(ModelResourceLocation.standalone(texture)));
        poseStack.popPose();
    }

    public static void renderModel(ItemStack stack, ItemDisplayContext displayContext, boolean leftHand, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay, ResourceLocation resourceLocation) {
        poseStack.pushPose();
        poseStack.translate(0.5F, 0.5F, 0.5F);
        BakedModel model = Minecraft.getInstance().getModelManager().getModel(ModelResourceLocation.standalone(resourceLocation));
        Minecraft.getInstance().getItemRenderer().render(stack, displayContext, leftHand, poseStack, buffer, light, overlay, model);
        poseStack.popPose();
    }

    public static void renderFluid(Level level, BlockState state, BlockPos pos, PotFluidHandler fluidHandler, PoseStack poseStack, MultiBufferSource multiBufferSource, int light, int overlay, ResourceLocation model, float height) {
        if (fluidHandler.isEmpty()) {
            return;
        }
        poseStack.pushPose();
        Fluid fluid = fluidHandler.getStoredFluid().getFluid();
        float yLevel = (float) fluidHandler.getStoredFluid().getAmount() / (float) fluidHandler.getCapacity();
        poseStack.translate(0.0F, (yLevel - 1.0F) * height, 0.0F);
        TextureAtlasSprite[] sprites = FluidSpriteCache.getFluidSprites(level, pos, fluid.defaultFluidState());
        BakedModel originalModel = Minecraft.getInstance().getModelManager().getModel(ModelResourceLocation.standalone(model));
        Minecraft.getInstance().getBlockRenderer().getModelRenderer().tesselateBlock(
                level, new VariableFluidBakedModel(originalModel, sprites[0]),
                state, pos, poseStack, multiBufferSource.getBuffer(RenderType.translucent()), true,
                RandomSource.create(), light, overlay, ModelData.EMPTY, RenderType.translucent());
        poseStack.popPose();
    }
}