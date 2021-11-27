package com.ucla.jam.music;

/**
 * Handles outcome of a discogs query.
 * @param <T> Type of result from query
 */
public interface ResultHandler<T> {
    /**
     * Called when a result is found successfully.
     * @param result The result object
     */
    void completed(T result);

    /**
     * Called when a result is not, and an exception happens instead.
     * @param error The error object
     */
    void failed(Throwable error);
}
