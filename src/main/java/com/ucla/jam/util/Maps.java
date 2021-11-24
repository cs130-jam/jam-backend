package com.ucla.jam.util;

import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

public class Maps {
    public static <T> Map<T, Integer> combineMaps(Map<T, Integer> mapA, Map<T, Integer> mapB) {
        return Stream.of(mapA, mapB)
                .flatMap(map -> map.entrySet().stream())
                .collect(toMap(Entry::getKey, Entry::getValue, Integer::sum));
    }
}
