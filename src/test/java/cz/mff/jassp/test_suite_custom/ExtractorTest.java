package cz.mff.jassp.test_suite_custom;

import org.junit.Test;
import cz.mff.jassp.processing.extractor.ExtractorException;
import cz.mff.jassp.processing.extractor.Extractor;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class ExtractorTest {

    @Test()
    public void intParserWorks() {
        var intParser = Extractor.Integer();

        assertTrue(intParser.validate("5"));
        assertTrue(intParser.validate("123456789"));
        assertTrue(intParser.validate("-12345"));

        assertFalse(intParser.validate("5a"));
        assertFalse(intParser.validate("55.55"));
        assertFalse(intParser.validate("abc"));

        assertEquals(5, (int) intParser.parse("5"));
        assertEquals(123456789, (int) intParser.parse("123456789"));
        assertEquals(-42, (int) intParser.parse("-42"));
    }

    @Test
    public void realParserTest() {
        var realParser = Extractor.Real();

        assertTrue(realParser.validate("123.456"));
        assertTrue(realParser.validate("123456"));
        assertTrue(realParser.validate("123e45"));
        assertTrue(realParser.validate("-12e-3"));

        assertFalse(realParser.validate("--24"));
        assertFalse(realParser.validate("2 2. 5"));

        assertEquals((Object) 123.5, realParser.parse("123.5"));
        assertEquals((Object) (-1e-1), realParser.parse("-1e-1"));

        assertThrows(ExtractorException.class, () -> realParser.parse("-1.e-1."));
        assertThrows(ExtractorException.class, () -> realParser.parse("05a2"));
    }

    @Test
    public void listParserTest() {
        var listParser = Extractor.List(",");

        assertTrue(listParser.validate("abc,def"));
        assertTrue(listParser.validate(""));

        var expectedSimpleList1 = Arrays.asList("abc", "def", "ghi");
        assertEquals(expectedSimpleList1, listParser.parse("abc,def,ghi"));

        var expectedSimpleList2 = List.of("abc");
        assertEquals(expectedSimpleList2, listParser.parse("abc"));
    }

    @Test
    public void parsedListParser() {
        var parsedListParser = Extractor.List(",", Extractor.Integer());

        assertTrue(parsedListParser.validate("5,2,4"));
        assertFalse(parsedListParser.validate("5,a,2"));

        var expectedSimpleList1 = Arrays.asList(5, 2, 4);
        assertEquals(expectedSimpleList1, parsedListParser.parse("5,2,4"));

        var nestedParser = Extractor.List(",",
                Extractor.List("-", Extractor.Integer()));

        assertTrue(nestedParser.validate("1-2,3-4,5-6"));
        assertFalse(nestedParser.validate("1,2,,3-4"));

        var expectedNestedList1 = Arrays.asList(
                Arrays.asList(1, 2),
                Arrays.asList(3, 4),
                Arrays.asList(5, 6)
        );
        assertEquals(expectedNestedList1, nestedParser.parse("1-2,3-4,5-6"));
    }

}