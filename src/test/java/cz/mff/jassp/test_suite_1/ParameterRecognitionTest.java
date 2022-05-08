package cz.mff.jassp.test_suite_1;

import org.junit.Before;
import org.junit.Test;
import cz.mff.jassp.option.*;


public class ParameterRecognitionTest extends ParserTest {

	static final String VERBOSE = "verbose";
	static final String DELIM = "delim";

	static final String V = "v";
	static final String D = "d";


	@Before
	public void setupOptions() {
		options.add(Option.builder().addShortAlias(V).addLongAlias(VERBOSE).build());
		options.add(Option.builder().addShortAlias(D).addLongAlias(DELIM).expectsParameter().build());
	}

	@Test	
	public void shortOptionSpaceSeparatedParamRecognized() {
		parseCommand("-d v");
		assertOptionHasParam(DELIM, "v");
	}

	@Test	
	public void longOptionSpaceSeparatedParamRecognized() {
		parseCommand("--delim v");
		assertOptionHasParam(DELIM, "v");
	}

}
