package cz.mff.jassp.test_suite_2.extractor;

import cz.mff.jassp.processing.extractor.ExtractorException;
import cz.mff.jassp.processing.extractor.Extractor;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static cz.mff.jassp.test_suite_2.Utils.arrayOf;
import static org.junit.Assert.*;

public final class ExtractorsTest {
    // TODO What exception should be thrown? I suggest inlining this constant after setting it to a more specific class.
    static final Class<? extends Exception> EXTRACTOR_FACTORY_EXCEPTION = RuntimeException.class;

    @Test
    public void canComposeStructListExtractors() {
        String input = "1,2,3.5;7;11,125.125";
        Extractor<List<List<Double>>> e = Extractor.List(
                ";",
                Extractor.List(
                        ",",
                        Extractor.Real()
                )
        );

        assertTrue(e.validate(input));
        assertArrayEquals(new Object[]{
                arrayOf(1., 2., 3.5),
                arrayOf(7.),
                arrayOf(11., 125.125),
        }, e.parse(input).stream().map(List::toArray).toArray());
    }

    @Test
    public void stringExtractorAcceptsAnyString() {
        Extractor<String> e = Extractor.String();

        assertTrue(e.validate("any String"));
        assertEquals("any String", e.parse("any String"));
    }

    @Test
    public void stringExtractorAcceptsAnyStringInDomain() {
        Extractor<String> e = Extractor.String(List.of("any String in domain"));

        assertTrue(e.validate("any String in domain"));
        assertEquals("any String in domain", e.parse("any String in domain"));
    }

    @Test
    public void stringExtractorRejectsAnyStringOutsideDomain() {
        Extractor<String> e = Extractor.String(List.of("any String in domain"));

        assertFalse(e.validate("any String outside domain"));
        assertThrows(ExtractorException.class, () -> e.parse("any String outside domain"));
    }

    @Test
    @SuppressWarnings("all")
    public void stringExtractorIsNotSensitiveToObjectIdentity() {
        String expected = new String("any String in domain");
        String passed = new String("any String in domain");
        Extractor<String> e = Extractor.String(List.of(expected));

        assertTrue(e.validate(passed));
        assertEquals(passed, e.parse(passed));
    }

    @Test
    public void stringExtractorWithNullElementFailsFast() {
        // note: List.of() checks for nulls
        List<String> list = Arrays.asList("foo", null, "baz");
        assertThrows(EXTRACTOR_FACTORY_EXCEPTION, () -> Extractor.String(list));
    }
}
