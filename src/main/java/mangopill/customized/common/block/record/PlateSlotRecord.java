package mangopill.customized.common.block.record;

public record PlateSlotRecord(int ingredientInput, int seasoningInput) {
    public static final PlateSlotRecord SOUP_BOWL = new PlateSlotRecord(6, 6);
}
