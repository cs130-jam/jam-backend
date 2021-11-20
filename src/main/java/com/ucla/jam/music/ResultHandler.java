package com.ucla.jam.music;

public interface ResultHandler<T> {
    void completed(T result);
    void failed(Throwable error);
}
