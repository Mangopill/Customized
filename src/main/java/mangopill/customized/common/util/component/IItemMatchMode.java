package mangopill.customized.common.util.component;

import java.util.function.BiPredicate;

@FunctionalInterface
public interface IItemMatchMode<T, U> {
    BiPredicate<T, U> getComparator();
}
