package cz.mff.jassp.test_suite_1;

import org.junit.Before;
import org.junit.Test;
import cz.mff.jassp.option.*;


public class OptionRecognitionTest extends ParserTest {

	static final String VERBOSE = "verbose";
	static final String APPEND = "append";

	static final String V = "v";
	static final String A = "a";


	@Before
	public void setupOptions() {
		options.add(Option.builder().addShortAlias(V).addLongAlias(VERBOSE).build());
		options.add(Option.builder().addShortAlias(A).addLongAlias(APPEND).build());
	}

	@Test
	public void definedShortOptionRecognized() {
		parseCommand("-v");
		assertDefinedOptionPresent(VERBOSE);
	}

	@Test
	public void definedLongOptionRecognized() {
		parseCommand("--verbose");
		assertDefinedOptionPresent(VERBOSE);
	}

	@Test
	public void spaceSeparatedDefinedShortOptionsRecognized() {
		parseCommand("-v -a");
		assertDefinedOptionsPresent(VERBOSE, APPEND);
	}

	@Test
	public void spaceSeparatedDefinedLongOptionsRecognized() {
		parseCommand("--verbose --append");
		assertDefinedOptionsPresent(VERBOSE, APPEND);
	}

	@Test
	public void definedShortAndLongOptionsRecognized() {
		parseCommand("-v --append");
		assertDefinedOptionsPresent(VERBOSE, APPEND);
	}

	@Test
	public void definedShortOptionNotMatchedWhenNotGiven() {
		parseCommand("-v");
		assertDefinedOptionNotPresent(APPEND);
	}

	@Test
	public void definedLongOptionNotMatchedWhenNotGiven() {
		parseCommand("--verbose");
		assertDefinedOptionNotPresent(APPEND);
	}

	@Test
	public void undefinedShortOptionNotMatchedWhenNotGiven() {
		parseCommand("-v");
		assertUndefinedOptionNotPresent("undefined");
	}

	@Test
	public void undefinedLongOptionNotMatchedWhenNotGiven() {
		parseCommand("--verbose");
		assertUndefinedOptionNotPresent("undefined");
	}
}
