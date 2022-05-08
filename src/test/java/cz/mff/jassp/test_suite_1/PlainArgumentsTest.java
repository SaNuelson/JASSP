package cz.mff.jassp.test_suite_1;

import org.junit.Before;
import org.junit.Test;
import cz.mff.jassp.option.*;


public class PlainArgumentsTest extends ParserTest {

	static final String VERBOSE = "verbose";
	static final String APPEND = "append";

	static final String V = "v";
	static final String A = "a";

	static final String FIRST = "first";
	static final String SECOND = "second";
	static final String DASHED = "-dashed";
	static final String TRICKY = "--verbose";


	@Before
	public void setupOptions() {
		options.add(Option.builder().addShortAlias(V).addLongAlias(VERBOSE).build());
		options.add(Option.builder().addShortAlias(A).addLongAlias(APPEND).build());
	}

	@Test
	public void loneArgumentRecognized() {
		parseCommand("first");
		assertPlainArgumentsEqual(FIRST);
	}

	@Test
	public void argumentAfterOptionsRecognized() {
		parseCommand("-v first");
		assertPlainArgumentsEqual(FIRST);
	}

	@Test
	public void argumentAfterDelimeterRecognized() {
		parseCommand("-- first");
		assertPlainArgumentsEqual(FIRST);
	}

	@Test
	public void dashedArgumentAfterDelimeterRecognized() {
		parseCommand("-- -dashed");
		assertPlainArgumentsEqual(DASHED);
	}

	@Test
	public void dashedArgumentSameNameAsOptionRecognized() {
		parseCommand("-- --verbose");
		assertPlainArgumentsEqual(TRICKY);
		assertDefinedOptionNotPresent(VERBOSE);
	}

	@Test
	public void commandEmptyNoArgumentsRecognized() {
		parseCommand("");
		assertPlainArgumentsEqual("");
	}

	@Test
	public void onlyDelimeterNoArgumentsRecognized() {
		parseCommand("--");
		assertPlainArgumentsEqual();
	}
}
