package cz.mff.jassp.test_suite_2.arg;

import cz.mff.jassp.parser.ParserException;
import cz.mff.jassp.option.OptionList;
import cz.mff.jassp.parser.ParsedArgList;
import org.junit.Test;

import static cz.mff.jassp.test_suite_2.Utils.arrayOf;
import static cz.mff.jassp.test_suite_2.Utils.parse;
import static org.junit.Assert.*;

public final class NoOptionsTest {
    // TODO What exception should be thrown? I suggest inlining this constant after setting it to a more specific class.
    private static final Class<? extends Exception> PARSED_ARG_LIST_EXCEPTION = RuntimeException.class;

    private final OptionList emptyOptionList = new OptionList();

    @Test
    public void emptyPlainArgsPreserved() {
        ParsedArgList result = parse(emptyOptionList);

        assertArrayEquals(arrayOf(), result.getPlainArgs().toArray());
    }

    @Test
    public void plainArgsPreserved() {
        ParsedArgList result = parse(emptyOptionList, "lorem", "ipsum");

        assertArrayEquals(arrayOf("lorem", "ipsum"), result.getPlainArgs().toArray());
    }

    @Test
    public void standardStreamArgPreserved() {
        ParsedArgList result = parse(emptyOptionList, "lorem", "-", "ipsum");

        assertArrayEquals(arrayOf("lorem", "-", "ipsum"), result.getPlainArgs().toArray());
    }

    @Test
    public void emptyStringArgPreserved() {
        ParsedArgList result = parse(emptyOptionList, "", "lorem", "ipsum");

        assertArrayEquals(arrayOf( "", "lorem", "ipsum"), result.getPlainArgs().toArray());
    }

    @Test
    public void delimiterParses() {
        ParsedArgList result = parse(emptyOptionList, "--", "lorem", "ipsum");

        assertArrayEquals(arrayOf("lorem", "ipsum"), result.getPlainArgs().toArray());
    }

    @Test
    public void delimiterStopsOptionParsing() {
        ParsedArgList result = parse(emptyOptionList, "--", "lorem", "--ipsum", "-d", "dolor");

        assertArrayEquals(arrayOf("lorem", "--ipsum", "-d", "dolor"), result.getPlainArgs().toArray());
    }

    @Test
    public void onlyFirstDelimiterParses() {
        ParsedArgList result = parse(emptyOptionList, "--", "lorem", "ipsum", "--", "dolor");

        assertArrayEquals(arrayOf("lorem", "ipsum", "--", "dolor"), result.getPlainArgs().toArray());
    }

    @Test
    public void undeclaredLongOptionThrows() {
        assertThrows(ParserException.class, () -> parse(emptyOptionList, "--ipsum", "lorem"));
    }

    @Test
    public void undeclaredShortOptionThrows() {
        assertThrows(ParserException.class, () -> parse(emptyOptionList, "-i", "lorem"));
    }

    @Test
    public void queryingUndeclaredLongOptionReturnsNull() {
        ParsedArgList result = parse(emptyOptionList, "lorem", "ipsum");

        assertNull(result.findArgument("dolor"));
    }

    @Test
    public void queryingUndeclaredShortOptionReturnsNull() {
        ParsedArgList result = parse(emptyOptionList, "lorem", "ipsum");

        assertNull(result.findArgument("d"));
    }
}
