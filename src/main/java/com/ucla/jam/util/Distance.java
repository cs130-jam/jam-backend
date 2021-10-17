package com.ucla.jam.util;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
public class Distance {
    float value;
    Unit units;

    @RequiredArgsConstructor
    public enum Unit {
        MILES("Miles"),
        KILOMETERS("Kilometers");

        @JsonValue
        public final String name;
    }
}
