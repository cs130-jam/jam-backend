package com.ucla.jam.util;

import lombok.SneakyThrows;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class Futures {
    @SneakyThrows({InterruptedException.class, ExecutionException.class})
    public static <T> T sneakyGet(Future<T> future) {
        return future.get();
    }
}
