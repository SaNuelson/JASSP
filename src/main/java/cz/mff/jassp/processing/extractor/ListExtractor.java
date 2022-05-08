package cz.mff.jassp.processing.extractor;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ListExtractor extends Extractor<List<String>> {

    private final String delim;

    /**
     * Creates a ListExtractor which parses strings to list using specified delimiter
     * @param delimiter used to split values
     */
    public ListExtractor(String delimiter) {
        if (delimiter == null || delimiter.equals("")) {
            throw new ExtractorException("ListExtractor delimiter cannot be null nor an empty string.");
        }

        delim = Pattern.quote(delimiter);
    }

    /**
     * Check whether matched string can be parsed into a list
     * @param match string of which value to validate.
     * @return True if list can be parsed, false otherwise
     */
    @Override
    public boolean validate(String match) {
        return match != null;
    }

    /**
     * Parse list of strings from provided matched string
     * @param match string of which value to parse.
     * @return Split string using specified delimiter, as a list of strings
     * @throws ExtractorException If matched value failed to be split.
     */
    @Override
    public List<String> parse(String match) throws ExtractorException {
        if (match == null)
            throw new ExtractorException("ListExtractor.parse argument is null");

        if (match.equals(""))
            return new ArrayList<>();

        return List.of(match.split(delim));
    }
}