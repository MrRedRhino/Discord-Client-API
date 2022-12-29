package org.pipeman.dccapi;

import java.util.Optional;

public class DCCAPIUtils {
    public static Optional<Long> parseLong(String s) {
        try {
            return Optional.of(Long.parseLong(s));
        } catch (NumberFormatException ignored) {
            return Optional.empty();
        }
    }

    public static <T> T getOrElse(T value, T fallback) {
        return value == null ? fallback : value;
    }
}
