package cz.mff.jassp.test_suite_2.extractor;

import cz.mff.jassp.processing.extractor.ExtractorException;
import cz.mff.jassp.processing.extractor.Extractor;
import org.junit.Test;

import static cz.mff.jassp.test_suite_2.extractor.ExtractorsTest.EXTRACTOR_FACTORY_EXCEPTION;
import static org.junit.Assert.*;

public final class IntExtractorTest {
    @Test
    public void acceptsUnboundedValue() {
        Extractor<Integer> e = Extractor.Integer();

        assertTrue(e.validate("42"));
        assertEquals(Integer.valueOf(42), e.parse("42"));
    }

    @Test
    public void acceptsUnboundedMinValue() {
        Integer minValue = Integer.MIN_VALUE;
        Extractor<Integer> e = Extractor.Integer();

        assertTrue(e.validate(minValue.toString()));
        assertEquals(minValue, e.parse(minValue.toString()));
    }

    @Test
    public void acceptsUnboundedMaxValue() {
        Integer maxValue = Integer.MAX_VALUE;
        Extractor<Integer> e = Extractor.Integer();

        assertTrue(e.validate(maxValue.toString()));
        assertEquals(maxValue, e.parse(maxValue.toString()));
    }

    @Test
    public void rejectsSpam() {
        Extractor<Integer> e = Extractor.Integer();

        assertFalse(e.validate("spam"));
        assertThrows(ExtractorException.class, () -> e.parse("spam"));
    }

    @Test
    public void acceptsValueInBounds() {
        Extractor<Integer> e = Extractor.Integer(40, 44);

        assertTrue(e.validate("42"));
        assertEquals(Integer.valueOf(42), e.parse("42"));
    }

    @Test
    public void rejectsBasedOnLowerBound() {
        Extractor<Integer> e = Extractor.Integer(40, 44);

        assertFalse(e.validate("38"));
        assertThrows(ExtractorException.class, () -> e.parse("38"));
    }

    @Test
    public void rejectsBasedOnUpperBound() {
        Extractor<Integer> e = Extractor.Integer(40, 44);

        assertFalse(e.validate("46"));
        assertThrows(ExtractorException.class, () -> e.parse("46"));
    }

    @Test
    public void lowerBoundIsInclusive() {
        Extractor<Integer> e = Extractor.Integer(40, 44);

        assertTrue(e.validate("40"));
        assertEquals(Integer.valueOf(40), e.parse("40"));
    }

    @Test
    public void upperBoundIsInclusive() {
        Extractor<Integer> e = Extractor.Integer(40, 44);

        assertTrue(e.validate("44"));
        assertEquals(Integer.valueOf(44), e.parse("44"));
    }

    @Test
    public void invertedBoundsFailFast() {
        assertThrows(EXTRACTOR_FACTORY_EXCEPTION, () -> Extractor.Integer(44, 40));
    }

    @Test // TODO remove this test if you want to allow options with single allowed value
    public void equalBoundsFailFast() {
        assertThrows(EXTRACTOR_FACTORY_EXCEPTION, () -> Extractor.Integer(42, 42));
    }
}
