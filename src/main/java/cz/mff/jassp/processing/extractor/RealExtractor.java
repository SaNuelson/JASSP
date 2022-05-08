package cz.mff.jassp.processing.extractor;

import java.util.List;

public class RealExtractor extends Extractor<Double> {

    private final double lowerBound;
    private final double upperBound;

    public static final List<String> positiveInfinityAliases = List.of(
            "Infinity",
            "+Infinity",
            "infinity",
            "+infinity",
            "Infty",
            "+Infty",
            "infty",
            "+infty",
            "Inf",
            "+Inf",
            "inf",
            "+inf"
    );

    public static final List<String> negativeInfinityAliases = List.of(
            "-Infinity",
            "-infinity",
            "-Infty",
            "-infty",
            "-Inf",
            "-inf"
    );

    /**
     * Construct RealExtractor with unbounded allowed values
     */
    public RealExtractor() {
        lowerBound = -Double.MAX_VALUE;
        upperBound = Double.MAX_VALUE;
    }

    /**
     * Construct RealExtractor with bounded allowed values
     * @param min lower exclusive bound of allowed values
     * @param max upper exclusive bound of allowed values
     */
    public RealExtractor(double min, double max) {
        if (Double.isNaN(min) || Double.isNaN(max)) {
            throw new ExtractorException("Bounds cannot be NaN.");
        }

        if (min >= max) {
            throw new ExtractorException("Min value cannot be larger than max value.");
        }

        lowerBound = min;
        upperBound = max;
    }

    @Override
    public boolean validate(String match) {
        try {

            if (positiveInfinityAliases.contains(match)) {
                return true;
            }

            if (negativeInfinityAliases.contains(match)) {
                return true;
            }

            double value = Double.parseDouble(match);
            if (lowerBound <= value && value <= upperBound) {
                return true;
            }
        }
        catch (NumberFormatException ignored) {}
        return false;
    }

    /**
     * Try parse string into double.
     * @param match string of which value to parse.
     * @return double value parsed
     * @throws ExtractorException when matched string cannot be parsed or its parsed value is out of bounds
     */
    @Override
    public Double parse(String match) throws ExtractorException {
        try {

            if (positiveInfinityAliases.contains(match)) {
                return Double.POSITIVE_INFINITY;
            }

            if (negativeInfinityAliases.contains(match)) {
                return Double.NEGATIVE_INFINITY;
            }

            double value = Double.parseDouble(match);
            if (lowerBound <= value && value <= upperBound) {
                return value;
            }
            throw new ExtractorException("Extracted double (" + match + ") is out of bounds (" + lowerBound + ", " + upperBound + ")");
        }
        catch(NumberFormatException e) {
            throw new ExtractorException("Failed to extract double from: " + match, e);
        }
    }
}