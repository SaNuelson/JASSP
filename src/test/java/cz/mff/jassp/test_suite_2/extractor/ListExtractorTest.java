package cz.mff.jassp.test_suite_2.extractor;

import cz.mff.jassp.processing.extractor.Extractor;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import static cz.mff.jassp.test_suite_2.Utils.arrayOf;
import static cz.mff.jassp.test_suite_2.extractor.ExtractorsTest.EXTRACTOR_FACTORY_EXCEPTION;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertThrows;

// TODO I had to "interpret" a lot. Please update these tests where necessary (and complete the documentation).
public final class ListExtractorTest {
    @Test
    public void emptyStringParsesToEmptyList() {
        Extractor<List<String>> e = Extractor.List(",");

        Assert.assertTrue(e.validate(""));
        assertArrayEquals(arrayOf(), e.parse("").toArray());
    }

    @Test
    public void nonDelimitedStringParsesToSingletonList() {
        Extractor<List<String>> e = Extractor.List(",");

        Assert.assertTrue(e.validate("spam"));
        assertArrayEquals(arrayOf("spam"), e.parse("spam").toArray());
    }

    @Test
    public void valuesSplitOnDelimiters() {
        Extractor<List<String>> e = Extractor.List(",");

        Assert.assertTrue(e.validate("foo,bar,baz"));
        assertArrayEquals(arrayOf("foo", "bar", "baz"), e.parse("foo,bar,baz").toArray());
    }

    @Test
    public void emptyStringsArePreserved() {
        Extractor<List<String>> e = Extractor.List(",");

        Assert.assertTrue(e.validate("foo,,baz"));
        assertArrayEquals(arrayOf("foo", "", "baz"), e.parse("foo,,baz").toArray());
    }

    // TODO Do not take String and use it as a regex. If it should be a regex, take an instance of java.util.regex.Pattern.
    //      It will be more self-documenting, it will fail fast on syntax errors, and (last,) it will be more efficient.
    //      In addition to .split(String), Pattern also has a .splitAsStream(String) method, which might be helpful.
    //      If it should not be a regex, pass the string through Pattern.quote().
    @Test
    public void delimitersAreStrings() {
        Extractor<List<String>> e = Extractor.List(".");

        Assert.assertTrue(e.validate("127.0.0.1"));

        var expected = arrayOf("127", "0", "0", "1");
        var actual = e.parse("127.0.0.1").toArray();
        assertArrayEquals(expected, actual);
    }

    @Test
    public void nullDelimiterFailsFast() {
        assertThrows(EXTRACTOR_FACTORY_EXCEPTION, () -> Extractor.List(null));
    }

    @Test
    public void emptyDelimiterFailsFast() {
        assertThrows(EXTRACTOR_FACTORY_EXCEPTION, () -> Extractor.List(""));
    }
}
