package mangopill.customized.client.util;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import mangopill.customized.common.block.AbstractPotBlock;
import mangopill.customized.common.block.entity.*;
import mangopill.customized.common.block.state.PotState;
import mangopill.customized.common.item.AbstractPlateItem;
import mangopill.customized.common.util.PropertyValueUtil;
import mangopill.customized.common.util.category.NutrientCategory;
import mangopill.customized.common.util.value.PropertyValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static mangopill.customized.common.util.ResourceUtil.*;
import static net.minecraft.client.renderer.entity.ItemRenderer.*;

public final class ClientUtil {
    private ClientUtil() {
    }

    public static int getMaxValueColor(Level level, List<ItemStack> stackList) {
        Map<NutrientCategory, Float> nutrientSums = new HashMap<>();
        for (ItemStack stack : stackList) {
            PropertyValue propertyValue = PropertyValueUtil.getPropertyValue(stack, level);
            if (propertyValue.isEmpty()) {
                continue;
            }
            for (Pair<NutrientCategory, Float> entry : propertyValue.toSet()) {
                NutrientCategory category = entry.getKey();
                float value = entry.getValue() * stack.getCount();
                nutrientSums.put(category, nutrientSums.getOrDefault(category, 0f) + value);
            }
        }
        NutrientCategory maxCategory = null;
        double maxSum = 0D;
        for (Map.Entry<NutrientCategory, Float> entry : nutrientSums.entrySet()) {
            if (entry.getValue() > maxSum) {
                maxSum = entry.getValue();
                maxCategory = entry.getKey();
            }
        }
        return maxCategory != null ? maxCategory.getColorWithAlpha() : 0xCC3F76E4;
    }

    public static void renderDrivePot(AbstractPotBlockEntity potBlockEntity, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay,
                                      float startLength, float startWidth, float startHeight, float endLength, float endWidth, float endHeight) {
        List<ItemStack> stackList = potBlockEntity.getItemStackListInPot(false, false);
        boolean dynamic = potBlockEntity.getBlockState().getValue(AbstractPotBlock.LID).equals(PotState.WITH_DRIVE);
        renderDrive(Objects.requireNonNull(potBlockEntity.getLevel()), stackList, poseStack, buffer, light, overlay, startLength, startWidth, startHeight, endLength, endWidth, endHeight, dynamic);
    }

    public static void renderDrivePlate(AbstractPlateBlockEntity plateBlockEntity, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay,
                                        float startLength, float startWidth, float startHeight, float endLength, float endWidth, float endHeight) {
        List<ItemStack> stackList = plateBlockEntity.getItemStackListInPlate(false);
        renderDrive(Objects.requireNonNull(plateBlockEntity.getLevel()), stackList, poseStack, buffer, light, overlay, startLength, startWidth, startHeight, endLength, endWidth, endHeight, true);
    }

    public static void renderDrivePlateItem(ItemStack stack, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay,
                                        float startLength, float startWidth, float startHeight, float endLength, float endWidth, float endHeight) {
        if (stack.getItem() instanceof AbstractPlateItem plateItem){
            List<ItemStack> stackList = plateItem.getItemStackListInPlate(stack, false);
            if (Minecraft.getInstance().player != null) {
                renderDrive(Minecraft.getInstance().player.level(), stackList, poseStack, buffer, light, overlay, startLength, startWidth, startHeight, endLength, endWidth, endHeight, true);
            }
        }
    }

    public static void renderDrive(Level level, List<ItemStack> stackList, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay,
                                   float startLength, float startWidth, float startHeight, float endLength, float endWidth, float endHeight, boolean dynamic) {
        float globalTime = level.getGameTime() % 24000 * 0.05F;
        for (ItemStack stack : stackList) {
            ItemStack newStack = stack.copy();
            if (!newStack.isEmpty()) {
                int count = newStack.getCount();
                int renderCount = count / 4 + (count % 4 > 0 ? 1 : 0);
                for (int i = 0; i < renderCount; i++) {
                    Random rand = new Random(stackList.lastIndexOf(stack) + i);
                    int seed = Item.getId(newStack.getItem()) + rand.nextInt() + i;
                    Random random = new Random(seed);
                    float randX = startLength + (random.nextFloat(endLength - startLength));
                    float baseY = startHeight + (random.nextFloat(endHeight - startHeight));
                    float randZ = startWidth + (random.nextFloat(endWidth - startWidth));
                    float phase = (seed % 1000) * 0.1F;
                    float deltaY = (float) Math.sin(globalTime * 0.8F + phase) * 0.03F;
                    float animatedY = dynamic ? baseY + deltaY : baseY;
                    poseStack.pushPose();
                    poseStack.translate(randX, animatedY, randZ);
                    poseStack.mulPose(Axis.ZP.rotationDegrees(random.nextFloat(360.0F)));
                    poseStack.mulPose(Axis.XP.rotationDegrees(random.nextFloat(360.0F)));
                    poseStack.mulPose(Axis.YP.rotationDegrees(random.nextFloat(360.0F)));
                    poseStack.scale(0.3F, 0.3F, 0.3F);
                    ItemStack renderItemStack = newStack.copy();
                    renderItemStack.setCount(1);
                    Minecraft.getInstance().getItemRenderer().renderStatic(renderItemStack, ItemDisplayContext.FIXED, light, overlay, poseStack, buffer, null, seed);
                    poseStack.popPose();
                }
            }
        }
    }

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

    public static void renderItemStack(ItemStack renderItemStack, PoseStack poseStack, MultiBufferSource buffer, @Nullable BlockState state, int light,
                                       int overlay, float startX, float startY, float startZ, float scaleX, float scaleY,
                                       float scaleZ, float degreesX, float degreesY, float degreesZ) {
        poseStack.pushPose();
        adjust(poseStack, state, startX, startY, startZ);
        poseStack.scale(scaleX, scaleY, scaleZ);
        poseStack.mulPose(Axis.XP.rotationDegrees(degreesX));
        poseStack.mulPose(Axis.YP.rotationDegrees(degreesY));
        poseStack.mulPose(Axis.ZP.rotationDegrees(degreesZ));
        if (!renderItemStack.isEmpty()) {
            renderItemStack.copy().setCount(1);
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

    public static <T extends LivingEntity, M extends EntityModel<T>> void renderHatModel(PoseStack poseStack, MultiBufferSource buffer, int packedLight, M model, ItemStack stack, float scale, double translateY, String path) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        poseStack.pushPose();
        if (model instanceof HumanoidModel<?> humanoidModel) {
            humanoidModel.head.translateAndRotate(poseStack);
        }
        poseStack.scale(scale, scale, scale);
        poseStack.translate(0.0D, translateY, 0.0D);
        poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
        ModelResourceLocation resourceLocation = ModelResourceLocation.standalone(getCLoc(path + BuiltInRegistries.ITEM.getKey(stack.getItem()).getPath()));
        itemRenderer.render(stack, ItemDisplayContext.NONE, false, poseStack, buffer, packedLight, OverlayTexture.NO_OVERLAY, itemRenderer.getItemModelShaper().getModelManager().getModel(resourceLocation));
        poseStack.popPose();
    }

    public static void renderModel(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource multiBufferSource, int light, int overlay, String path) {
        BakedModel model = Minecraft.getInstance().getItemRenderer()
                .getItemModelShaper().getModelManager().getModel(ModelResourceLocation.standalone(
                        getCLoc(path)
                ));
        boolean flag1;
        label78:
        {
            if (displayContext != ItemDisplayContext.GUI && !displayContext.firstPerson()) {
                Item item = stack.getItem();
                if (item instanceof BlockItem blockItem) {
                    Block block = blockItem.getBlock();
                    flag1 = !(block instanceof HalfTransparentBlock) && !(block instanceof StainedGlassPaneBlock);
                    break label78;
                }
            }
            flag1 = true;
        }
        for (BakedModel passModel : model.getRenderPasses(stack, flag1)) {
            for (RenderType renderType : passModel.getRenderTypes(stack, flag1)) {
                VertexConsumer vertexConsumer;
                if (flag1) {
                    vertexConsumer = getFoilBufferDirect(multiBufferSource, renderType, true, stack.hasFoil());
                } else {
                    vertexConsumer = getFoilBuffer(multiBufferSource, renderType, true, stack.hasFoil());
                }
                Minecraft.getInstance().getItemRenderer().renderModelLists(passModel, stack, light, overlay, poseStack, vertexConsumer);
            }
        }
    }
}