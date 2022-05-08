package cz.mff.jassp.processing.extractor;

import java.util.List;
import java.util.Objects;

public class StringExtractor extends Extractor<String> {
    // TODO: Apart from domain, maybe regexes etc.?

    private final List<String> domain;

    /**
     * Create an instance of string extractor with unbounded accepted values.
     */
    public StringExtractor() {
        domain = null;
    }

    /**
     * Create an instance of string extractor with bounded accepted values by a domain.
     * @param allowedValues list of strings which are allowed
     */
    public StringExtractor(List<String> allowedValues) {
        if (allowedValues == null)
            throw new ExtractorException("StringExtractor domain cannot be null (for unbounded String extractor use parameterless constructor)");
        if (allowedValues.stream().anyMatch(Objects::isNull))
            throw new ExtractorException("StringExtractor domain cannot contain null");
        domain = allowedValues;
    }

    /**
     * Check whether matched string adheres to extractor's conditions (falls in specified domain, if any)
     * @param match string of which value to validate.
     * @return True if matched string is within domain, false otherwise
     */
    @Override
    public boolean validate(String match) {
        if (match == null)
            return false;

        if (domain != null)
            return domain.contains(match);

        return true;
    }

    /**
     * Try parse string into integer.
     * @param match string of which value to parse.
     * @return integer value parsed
     * @throws ExtractorException when matched string cannot be parsed or its parsed value is out of bounds
     */
    @Override
    public String parse(String match) throws ExtractorException {
        if (validate(match)) {
            return match;
        }
        else {
            throw new ExtractorException("Extracted string (" + match + ") doesn't belong to specified domain or is null.");
        }
    }
}