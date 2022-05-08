package cz.mff.jassp.test_suite_1;

import cz.mff.jassp.parser.ParserException;
import cz.mff.jassp.processing.extractor.ExtractorException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import cz.mff.jassp.option.*;
import cz.mff.jassp.processing.extractor.Extractor;


public class ExtractorTest extends ParserTest {

	static final String ALIAS = "alias";
	static final String A = "a";

	@Test
	public void intExtractorValidInput() {
		options.add(Option.builder().addShortAlias(A).setExtractor(Extractor.Integer()).build());
		parseCommand("-a 5");

		Integer actual = (Integer)parsedList.getValue(A);
		Integer expected = 5;
		assertEquals(expected, actual);
	}

	@Test(expected = ExtractorException.class)
	public void intExtractorInvalidInput() {
		options.add(Option.builder().addShortAlias(A).setExtractor(Extractor.Integer()).build());
		parseCommand("-a x");
	}
}
