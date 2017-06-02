package com.askdog.common.utils;

import java.util.function.Supplier;

public class Conditions {

    public static <T extends Throwable> void checkState(boolean expression, Supplier<? extends T> exceptionSupplier) throws T {
        if (!expression) {
            throw exceptionSupplier.get();
        }
    }

}
