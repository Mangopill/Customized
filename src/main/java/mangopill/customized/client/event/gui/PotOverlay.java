package mangopill.customized.client.event.gui;

import mangopill.customized.common.block.entity.AbstractPotBlockEntity;
import mangopill.customized.common.registry.CItemRegistry;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import javax.annotation.Nullable;

import static mangopill.customized.common.CustomizedConfig.*;
import static mangopill.customized.common.util.StringUtil.*;

public class PotOverlay implements LayeredDraw.Layer {
    @Nullable
    private static AbstractPotBlockEntity getTargetPot() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.hitResult == null || mc.hitResult.getType() != HitResult.Type.BLOCK) {
            return null;
        }
        BlockPos pos = ((BlockHitResult) mc.hitResult).getBlockPos();
        BlockEntity blockEntity = mc.level.getBlockEntity(pos);
        if (blockEntity instanceof AbstractPotBlockEntity potBlockEntity) {
            return potBlockEntity;
        }
        return null;
    }

    @Override
    public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        AbstractPotBlockEntity pot = getTargetPot();
        if (pot == null || !pot.hasInput() || !POT_OVERLAY.get()) return;
        int screenWidth = guiGraphics.guiWidth();
        int screenHeight = guiGraphics.guiHeight();
        int elementWidth = 128;
        int elementHeight = 6;
        int centerX = (screenWidth - elementWidth) / 2;
        int centerY = (screenHeight - elementHeight) / 100 * 90;
        int centerY1 = (screenHeight - elementHeight) / 100 * 95;
        int cookingProgress = pot.getCookingCompletionTime() >= 0 ? Math.min(elementWidth, Math.max(1, (int) (elementWidth * (float) pot.getCookingTime() / pot.getCookingCompletionTime()))) : 0;
        int customizedProgress = pot.getCustomizedCompletionTime() >= 0 ? Math.min(elementWidth, Math.max(1, (int) (elementWidth * (float) pot.getCustomizedTime() / pot.getCustomizedCompletionTime()))) : 0;
        guiGraphics.pose().pushPose();
        guiGraphics.blit(getCPngLoc("textures/gui/pot_background"), centerX, centerY, elementWidth, elementHeight, 0, 0, elementWidth, elementHeight, elementWidth, elementHeight);
        guiGraphics.blitSprite(getCLoc("pot_progress_2"), elementWidth, elementHeight, 0, 0, centerX, centerY, cookingProgress, elementHeight);
        guiGraphics.renderItem(CItemRegistry.FAMOUS_DISH_PLATE.get().getDefaultInstance(), centerX - 20, centerY + (elementHeight - 16) / 2);
        guiGraphics.blit(getCPngLoc("textures/gui/pot_background"), centerX, centerY1, elementWidth, elementHeight, 0, 0, elementWidth, elementHeight, elementWidth, elementHeight);
        guiGraphics.blitSprite(getCLoc("pot_progress_1"), elementWidth, elementHeight, 0, 0, centerX, centerY1, customizedProgress, elementHeight);
        guiGraphics.renderItem(CItemRegistry.CHEF_HAT.get().getDefaultInstance(), centerX - 20, centerY1 + (elementHeight - 16) / 2);
        guiGraphics.pose().popPose();
    }
}