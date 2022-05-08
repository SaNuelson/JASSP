package cz.mff.jassp.test_suite_2.extractor;

import cz.mff.jassp.processing.extractor.ExtractorException;
import cz.mff.jassp.processing.extractor.Extractor;
import org.junit.Test;

import static cz.mff.jassp.test_suite_2.extractor.ExtractorsTest.EXTRACTOR_FACTORY_EXCEPTION;
import static org.junit.Assert.*;

public final class RealExtractorTest {
    @Test
    public void acceptsUnboundedValue() {
        Extractor<Double> e = Extractor.Real();

        assertTrue(e.validate("4.2"));
        assertEquals(Double.valueOf(4.2), e.parse("4.2"));
    }

    @Test
    public void acceptsUnboundedMinValue() {
        Double minValue = Double.MIN_VALUE;
        Extractor<Double> e = Extractor.Real();

        assertTrue(e.validate(minValue.toString()));
        assertEquals(minValue, e.parse(minValue.toString()));
    }

    @Test
    public void acceptsUnboundedMaxValue() {
        Double maxValue = Double.MAX_VALUE;
        Extractor<Double> e = Extractor.Real();

        assertTrue(e.validate(maxValue.toString()));
        assertEquals(maxValue, e.parse(maxValue.toString()));
    }

    @Test
    public void acceptsUnboundedNegInfinity() {
        Extractor<Double> e = Extractor.Real();

        assertTrue(e.validate("-Infinity"));
        assertEquals(Double.valueOf(Double.NEGATIVE_INFINITY), e.parse("-Infinity"));
    }

    @Test
    public void acceptsUnboundedPosInfinity() {
        Extractor<Double> e = Extractor.Real();

        assertTrue(e.validate("+Infinity"));
        assertEquals(Double.valueOf(Double.POSITIVE_INFINITY), e.parse("+Infinity"));
    }

    @Test
    public void rejectsSpam() {
        Extractor<Double> e = Extractor.Real();

        assertFalse(e.validate("spam"));
        assertThrows(ExtractorException.class, () -> e.parse("spam"));
    }

    @Test
    public void acceptsValueInBounds() {
        Extractor<Double> e = Extractor.Real(4.0, 4.4);

        assertTrue(e.validate("4.2"));
        assertEquals(Double.valueOf(4.2), e.parse("4.2"));
    }

    @Test
    public void rejectsBasedOnLowerBound() {
        Extractor<Double> e = Extractor.Real(4.0, 4.4);

        assertFalse(e.validate("3.8"));
        assertThrows(ExtractorException.class, () -> e.parse("3.8"));
    }

    @Test
    public void rejectsBasedOnUpperBound() {
        Extractor<Double> e = Extractor.Real(4.0, 4.4);

        assertFalse(e.validate("4.6"));
        assertThrows(ExtractorException.class, () -> e.parse("4.6"));
    }

    @Test
    public void lowerBoundIsInclusive() {
        Extractor<Double> e = Extractor.Real(4.0, 4.4);

        assertTrue(e.validate("4.0"));
        assertEquals(Double.valueOf(4.0), e.parse("4.0"));
    }

    @Test
    public void upperBoundIsInclusive() {
        Extractor<Double> e = Extractor.Real(4.0, 4.5);

        assertTrue(e.validate("4.5"));
        assertEquals(Double.valueOf(4.5), e.parse("4.5"));
    }

    @Test
    public void rejectsBoundedNaN() {
        Extractor<Double> e = Extractor.Real(4.0, 4.5);

        assertFalse(e.validate("NaN"));
        assertThrows(ExtractorException.class, () -> e.parse("NaN"));
    }

    @Test
    public void invertedBoundsFailFast() {
        assertThrows(EXTRACTOR_FACTORY_EXCEPTION, () -> Extractor.Real(4.4, 4.0));
    }

    @Test // TODO remove this test if you want to allow options with single allowed value
    public void equalBoundsFailFast() {
        assertThrows(EXTRACTOR_FACTORY_EXCEPTION, () -> Extractor.Real(4.2, 4.2));
    }

    @Test
    public void nanLowerBoundFailsFast() {
        assertThrows(EXTRACTOR_FACTORY_EXCEPTION, () -> Extractor.Real(Double.NaN, 4.2));
    }

    @Test
    public void nanUpperBoundFailsFast() {
        assertThrows(EXTRACTOR_FACTORY_EXCEPTION, () -> Extractor.Real(4.2, Double.NaN));
    }
}
