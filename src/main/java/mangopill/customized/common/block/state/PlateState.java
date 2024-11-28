package mangopill.customized.common.block.state;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum PlateState implements StringRepresentable {
    WITHOUT_DRIVE("without_drive"),
    WITH_DRIVE("with_drive");

    private final String name;

    PlateState(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.getSerializedName();
    }

    @Override
    public @NotNull String getSerializedName() {
        return this.name;
    }
}
