package mangopill.customized.client.util.strategy;

import com.mojang.blaze3d.vertex.PoseStack;
import mangopill.customized.common.block.handler.PotFluidHandler;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.client.textures.FluidSpriteCache;

import javax.annotation.Nullable;
import java.util.List;

import static mangopill.customized.client.util.ClientUtil.*;

public record RenderFluidInBlockStrategy(@Nullable Level level, BlockPos pos, PotFluidHandler fluidHandler, List<ItemStack> stackList,
                                         float minX, float minY, float minZ, float maxX, float maxY, float maxZ, boolean solid) implements IRenderStrategy {
    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {
        if (fluidHandler.isEmpty() || level == null) return;
        poseStack.pushPose();
        Fluid fluid = fluidHandler.getStoredFluid().getFluid();
        float yLevel = (float) fluidHandler.getStoredFluid().getAmount() / (float) fluidHandler.getCapacity();
        float fluidHeight = yLevel * (maxY - minY) ;
        TextureAtlasSprite[] sprites = FluidSpriteCache.getFluidSprites(level, pos, fluid.defaultFluidState());
        int color = IClientFluidTypeExtensions.of(fluid.defaultFluidState()).getTintColor(fluidHandler.getStoredFluid());
        renderFluidLayer(buffer.getBuffer(solid ? RenderType.solid() : ItemBlockRenderTypes.getRenderLayer(fluid.defaultFluidState())), poseStack, fluidHeight + minY,
                minX, minZ, maxX, maxZ, color, light, overlay, sprites[0]);
        poseStack.popPose();
    }
}
