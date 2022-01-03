package pl.crystalek.crcapi.core.util;

import lombok.experimental.UtilityClass;

import java.util.Optional;

@UtilityClass
public class NumberUtil {

    public Optional<Long> getLong(final Object number) {
        try {
            return Optional.of(Long.parseLong(number.toString()));
        } catch (final NumberFormatException exception) {
            return Optional.empty();
        }
    }

    public Optional<Short> getShort(final Object number) {
        try {
            return Optional.of(Short.parseShort(number.toString()));
        } catch (final NumberFormatException exception) {
            return Optional.empty();
        }
    }

    public Optional<Integer> getInt(final Object number) {
        try {
            return Optional.of(Integer.parseInt(number.toString()));
        } catch (final NumberFormatException exception) {
            return Optional.empty();
        }
    }

    public Optional<Double> getDouble(final Object number) {
        try {
            return Optional.of(Double.parseDouble(number.toString()));
        } catch (final NumberFormatException exception) {
            return Optional.empty();
        }
    }

    public Optional<Float> getFloat(final Object number) {
        try {
            return Optional.of(Float.parseFloat(number.toString()));
        } catch (final NumberFormatException exception) {
            return Optional.empty();
        }
    }
}
