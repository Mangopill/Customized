package mangopill.customized.common.util;

public final class FunctionalHelper {
    private FunctionalHelper() {}

    @FunctionalInterface
    public interface QuadConsumer<T, U, V, W> {
        void accept(T t, U u, V v, W w);
    }
}
