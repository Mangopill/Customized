package mangopill.customized.client.event.model;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class VariableFluidBakedModel implements BakedModel {
    private final TextureAtlasSprite fluidTexture;
    private final BakedModel originalModel;

    public VariableFluidBakedModel(BakedModel originalModel, TextureAtlasSprite fluidTexture) {
        this.originalModel = originalModel;
        this.fluidTexture = fluidTexture;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, RandomSource rand) {
        List<BakedQuad> originalQuads = originalModel.getQuads(state, side, rand);
        return originalQuads.stream().map(this::remapQuadTexture).toList();
    }

    private BakedQuad remapQuadTexture(BakedQuad originalQuad) {
        int[] vertexData = originalQuad.getVertices().clone();
        int vertexSize = 8;
        float[] uValues = {fluidTexture.getU0(), fluidTexture.getU1(), fluidTexture.getU1(), fluidTexture.getU0()};
        float[] vValues = {fluidTexture.getV1(), fluidTexture.getV1(), fluidTexture.getV0(), fluidTexture.getV0()};
        for (int i = 0; i < vertexData.length; i += vertexSize) {
            int vertexIndex = (i / vertexSize) % 4;
            vertexData[i + 4] = Float.floatToRawIntBits(uValues[vertexIndex]);
            vertexData[i + 5] = Float.floatToRawIntBits(vValues[vertexIndex]);
        }
        return new BakedQuad(vertexData, originalQuad.getTintIndex(), originalQuad.getDirection(), fluidTexture, originalQuad.isShade());
    }

    @Override
    public boolean useAmbientOcclusion() {
        return originalModel.useAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return originalModel.isGui3d();
    }

    @Override
    public boolean usesBlockLight() {
        return originalModel.usesBlockLight();
    }

    @Override
    public boolean isCustomRenderer() {
        return originalModel.isCustomRenderer();
    }

    @Override
    public TextureAtlasSprite getParticleIcon() {
        return originalModel.getParticleIcon();
    }

    @Override
    public ItemOverrides getOverrides() {
        return originalModel.getOverrides();
    }
}
