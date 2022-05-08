package cz.mff.jassp.test_suite_1;

import cz.mff.jassp.parser.ParserException;
import org.junit.Test;
import cz.mff.jassp.option.*;


public class ExceptionTest extends ParserTest {

	static final String SILENT = "silent";
	static final String QUIET = "quiet";
	static final String S = "s";
	static final String Q = "q";


	@Test(expected = BuilderException.class)
	public void optionWithNoAliases() {
		Option.builder().build();
	}

	@Test(expected = BuilderException.class)
	public void optionWithOnlyHandler() {
		Option.builder().setHandler(QUIET).build();
	}

	@Test(expected = BuilderException.class)
	public void optionWithOnlyShortAliases() {
		Option.builder().addShortAlias(Q).addShortAlias(S).build();
	}

	@Test(expected = BuilderException.class)
	public void optionWithOnlyLongAliases() {
		Option.builder().addLongAlias(QUIET).addLongAlias(SILENT).build();
	}

	@Test(expected = BuilderException.class)
	public void longStringAsShortAlias() {
		Option.builder().addShortAlias(QUIET).build();
	}

	@Test(expected = ParserException.class)
	public void optionsWithIdenticalShortAliases() {
		options.add(Option.builder().addShortAlias(Q).build());
		options.add(Option.builder().addShortAlias(Q).build());
		parseCommand("");
	}

	@Test(expected = ParserException.class)
	public void optionsWithIdenticalLongAliases() {
		options.add(Option.builder().addLongAlias(QUIET).build());
		options.add(Option.builder().addLongAlias(QUIET).build());
		parseCommand("");
	}

	@Test(expected = BuilderException.class)
	public void optionsWithIdenticalHandles() {
		options.add(Option.builder().setHandler(QUIET).build());
		options.add(Option.builder().setHandler(QUIET).build());
		parseCommand("");
	}

	@Test(expected = BuilderException.class)
	public void longAliasSameAsAnotherOptionHandle() {
		options.add(Option.builder().setHandler(QUIET).build());
		options.add(Option.builder().addLongAlias(QUIET).build());
		parseCommand("");
	}
}
