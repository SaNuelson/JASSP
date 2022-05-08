package cz.mff.jassp.test_suite_1;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import cz.mff.jassp.option.*;


public class HandleTest extends ParserTest {

	static final String VERBOSE = "verbose";
	static final String V = "v";


	@Test
	public void handleInferredFromShortAlias() {
		Option o = Option.builder().addShortAlias(V).build();
		String expectedHandle = V;
		assertEquals(expectedHandle, o.getHandler());		
	}

	@Test
	public void handleInferredFromLongAlias() {
		Option o = Option.builder().addLongAlias(VERBOSE).build();
		String expectedHandle = VERBOSE;
		assertEquals(expectedHandle, o.getHandler());		
	}

	@Test
	public void handleInferredFromShortAndLongAlias() {
		Option o = Option.builder().addShortAlias(V).addLongAlias(VERBOSE).build();
		String expectedHandle = VERBOSE;
		assertEquals(expectedHandle, o.getHandler());		
	}

	@Test
	public void handleInferredFromGivenHandle() {
		Option o = Option.builder().addShortAlias(V).setHandler(VERBOSE).build();
		String expectedHandle = VERBOSE;
		assertEquals(expectedHandle, o.getHandler());		
	}
}
