package mangopill.customized.common.block.record;

public record PlateSlotRecord(int ingredientInput, int seasoningInput, int spiceInput) {
    public static final PlateSlotRecord SOUP_BOWL = new PlateSlotRecord(6, 6, 1);
    public static final PlateSlotRecord BAKING_PAN = new PlateSlotRecord(5, 5, 1);
}
