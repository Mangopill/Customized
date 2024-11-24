package mangopill.customized.common.block.state;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum PotState implements StringRepresentable {
    WITHOUT_LID("without_lid"),
    WITH_LID("with_lid"),
    WITH_DRIVE("with_drive");

    private final String name;

    PotState(String name) {
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
