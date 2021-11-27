package com.ucla.jam.util;

import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

public class Maps {
    /**
     * Combine two maps from some key to integers by summing duplicate keys in the two maps.
     * @param mapA First map
     * @param mapB Second map
     * @param <T> Key type
     * @return Combined map
     */
    public static <T> Map<T, Integer> combineMaps(Map<T, Integer> mapA, Map<T, Integer> mapB) {
        return Stream.of(mapA, mapB)
                .flatMap(map -> map.entrySet().stream())
                .collect(toMap(Entry::getKey, Entry::getValue, Integer::sum));
    }
}
