package mangopill.customized.common.block.record;

public record PlateSlotRecord(int ingredientInput, int seasoningInput, int spiceInput) {
    public static final PlateSlotRecord SOUP_BOWL = new PlateSlotRecord(6, 6, 1);
}
