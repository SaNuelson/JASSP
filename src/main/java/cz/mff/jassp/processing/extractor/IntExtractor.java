package cz.mff.jassp.processing.extractor;

public class IntExtractor extends Extractor<Integer> {

    private final int lowerBound;
    private final int upperBound;

    /**
     * Create an instance of integer extractor with unbounded accepted values.
     */
    IntExtractor() {
        lowerBound = Integer.MIN_VALUE;
        upperBound = Integer.MAX_VALUE;
    }

    /**
     * Create an instance of integer extractor with bounded accepted values.
     *
     * @param min inclusive lower bound of accepted integers
     * @param max inclusive upper bound of accepted integers
     * @throws ExtractorException if min is greater or equal to max
     */
    IntExtractor(int min, int max) throws ExtractorException{
        if (min >= max) {
            throw new ExtractorException("Min value cannot be larger than max value.");
        }

        lowerBound = min;
        upperBound = max;
    }

    @Override
    public boolean validate(String match) {
        try {
            int value = Integer.parseInt(match);
            if (lowerBound <= value && value <= upperBound) {
                return true;
            }
        }
        catch (NumberFormatException ignored) {}
        return false;
    }

    /**
     * Try parse string into integer.
     * @param match string of which value to parse.
     * @return integer value parsed
     * @throws ExtractorException when matched string cannot be parsed or its parsed value is out of bounds
     */
    @Override
    public Integer parse(String match) throws ExtractorException {
        try {
            int value = Integer.parseInt(match);
            if (lowerBound <= value && value <= upperBound) {
                return value;
            }
            throw new ExtractorException("Extracted int (" + match + ") is out of bounds (" + lowerBound + ", " + upperBound + ")");
        }
        catch(NumberFormatException e) {
            throw new ExtractorException("Failed to extract integer from: " + match, e);
        }
    }
}