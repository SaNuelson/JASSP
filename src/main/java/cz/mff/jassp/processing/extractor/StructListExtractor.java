package cz.mff.jassp.processing.extractor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class StructListExtractor<X> extends Extractor<List<X>> {

    String delim;
    Extractor<X> partParser;

    /**
     * Create a nested StructListExtractor capable of parsing matched string into list of values parsed by nested extractor.
     * @param delimiter used to split values in matched string
     * @param innerParser parser to iteratively run on each split value
     */
    public StructListExtractor(String delimiter, Extractor<X> innerParser) {
        if (delimiter == null || delimiter.equals(""))
            throw new ExtractorException("StructListExtractor delimiter cannot be null nor an empty string");

        if (innerParser == null)
            throw new ExtractorException("StructListExtractor's nested extractor cannot be null");

        delim = delimiter;
        partParser = innerParser;
    }

    /**
     * Check whether matched string can be successfully parsed using this extractor as well as nested one.
     * @param match string of which value to validate.
     * @return True if matched string can be parsed, false otherwise.
     */
    @Override
    public boolean validate(String match) {
        var subMatches = match.split(delim);
        return Arrays.stream(subMatches).allMatch(subMatch -> partParser.validate(subMatch));
    }

    /**
     * Parse matched string into list of values parsed by nested parser.
     * @param match string of which value to parse.
     * @return parsed list
     * @throws ExtractorException if matched string cannot be parsed
     */
    @Override
    public List<X> parse(String match) throws ExtractorException {
        var subMatches = match.split(delim);
        return Arrays.stream(subMatches).map(subMatch -> partParser.parse(subMatch)).collect(Collectors.toList());
    }
}
