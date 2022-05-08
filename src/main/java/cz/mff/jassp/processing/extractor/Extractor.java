package cz.mff.jassp.processing.extractor;

import java.util.List;

/**
 * An extensible class used within Options. It accepts matched value or values and returns a parsed value to its type T.
 */
public abstract class Extractor<T> {
    /**
     * Validate whether the string value is convertible to type T.
     *
     * @param match string of which value to validate.
     * @return True if value is parsable, false otherwise.
     */
    public abstract boolean validate(String match);

    /**
     * Try parsing the string's value
     *
     * @param match string of which value to parse.
     * @return Parsed value as either instance of T in case T is reference type, value of type T otherwise.
     * @throws ExtractorException if value failed to be parsed. This behavior is unintended, as {@link #validate(String match)} should be used beforehand.
     */
    public abstract T parse(String match) throws ExtractorException;

    //region Built-in factories

    /**
     * Create an instance of a built-in extractor for options with string values.
     * @apiNote Adding this extractor to option builder is equivalent to calling
     * {@see cz.mff.java_arg_parser.option.OptionBuilder#expectsParameter}
     * @return a new instance of string extractor
     */
    public static Extractor<String> String() { return new StringExtractor(); }

    /**
     * Create an instance of a built-in extractor for options with string values.
     * @param allowedValues domain of allowed strings. If null, all strings are allowed
     * @return a new instance of string extractor
     */
    public static Extractor<String> String(List<String> allowedValues) { return new StringExtractor(allowedValues); }

    /**
     * Create an instance of a built-in extractor for options with integer values.
     * @return a new instance of int extractor
     */
    public static Extractor<Integer> Integer() { return new IntExtractor(); }

    /**
     * Create an instance of a built-in extractor for options with integer values.
     * @param min exclusive lower bound of allowed values
     * @param max exclusive upper bound of allowed values
     * @return a new instance of int extractor
     */
    public static Extractor<Integer> Integer(int min, int max) { return new IntExtractor(min, max); }

    /**
     * Create an instance of a built-in extractor for options with real (double) values.
     * @return a new instance of real extractor
     */
    public static Extractor<Double> Real() { return new RealExtractor(); }

    /**
     * Create an instance of a built-in extractor for options with real (double) values.
     * @param min exclusive lower bound of allowed values
     * @param max exclusive upper bound of allowed values
     * @return a new instance of real extractor
     */
    public static Extractor<Double> Real(double min, double max) { return new RealExtractor(min, max); }

    /**
     * Create an instance of built-in extractor for options with list values
     * @param delimiter a delimiter used to split values
     * @return a new instance of list extractor
     */
    public static Extractor<List<String>> List(String delimiter) { return new ListExtractor(delimiter); }

    /**
     * Create an instance of built-in extractor for options with list of recursively extracted values
     * @param delimiter a delimiter used to split values
     * @param innerParser an extractor which to use on each element of the extracted list
     * @return a new instance of recursive list extractor
     */
    public static <X> Extractor<List<X>> List(String delimiter, Extractor<X> innerParser) {
        return new StructListExtractor<>(delimiter, innerParser);
    }

    //endregion
}
