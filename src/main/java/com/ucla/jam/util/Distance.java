package com.ucla.jam.util;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
public class Distance {
    private static final double METERS_IN_MILE = 1609.34;
    private static final double METERS_IN_KILOMETER = 1000.0;

    double value;
    Unit units;

    public long toMeters() {
        switch (units) {
            case MILES:
                return (long) Math.floor(value * METERS_IN_MILE);
            case KILOMETERS:
                return (long) Math.floor(value * METERS_IN_KILOMETER);
            default:
                return 0;
        }
    }

    @RequiredArgsConstructor
    public enum Unit {
        MILES("Miles"),
        KILOMETERS("Kilometers");

        @JsonValue
        public final String name;
    }
}
