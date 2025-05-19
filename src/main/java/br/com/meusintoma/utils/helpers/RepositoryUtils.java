package br.com.meusintoma.utils.helpers;

import java.util.Optional;
import java.util.function.Supplier;

public class RepositoryUtils {
    public static <T> T findOrThrow(Optional<T> optional, Supplier<? extends RuntimeException> exceptionSupplier) {
        return optional.orElseThrow(exceptionSupplier);
    }
}
