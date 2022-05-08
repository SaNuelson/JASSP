package cz.mff.jassp.test_suite_1;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import static org.junit.Assert.*;

import cz.mff.jassp.option.*;
import cz.mff.jassp.parser.*;
import cz.mff.jassp.processing.logic.Rule;

/**
 * base class for some of the tests
 * provides custom assert methods for its children
 */
public class ParserTest {

	List<Option> options;
	List<Rule> rules;
	ParsedArgList parsedList;

	@Before
	public void resetParser() {
		options = new ArrayList<>();
		rules = new ArrayList<>();
		parsedList = null;
	}

	void parseCommand(String command) {

		ArgumentParser parser = new ArgumentParser();
		OptionList optionList = new OptionList();
		optionList.addOptions(options);
		optionList.addRules(rules);

		String[] args = command.split(" ");
		parsedList = parser.parse(optionList, args);

		assertNotNull(parsedList);
	}

	void assertDefinedOptionNotPresent(String handle) {
		assertFalse(parsedList.isPresent(handle));
		assertNotNull(parsedList.findArgument(handle));
	}

	void assertDefinedOptionPresent(String handle) {
		assertTrue(parsedList.isPresent(handle));
		assertNotNull(parsedList.findArgument(handle));
	}

	void assertUndefinedOptionNotPresent(String handle) {
		assertFalse(parsedList.isPresent(handle));
		assertNull(parsedList.findArgument(handle));
	}

	void assertDefinedOptionsPresent(String... handles) {
		for (String handle : handles) {
			assertDefinedOptionPresent(handle);
		}
	}
	
	void assertOptionHasParam(String handle, String expectedValue) {
		assertDefinedOptionsPresent(handle);
		String actualValue = parsedList.findArgument(handle).getRawValue();
		assertEquals(expectedValue, actualValue);
	}

	void assertPlainArgumentsEqual(String... expectedValues) {
		List<String> plainArgs = parsedList.getPlainArgs();
		assertNotNull(plainArgs);
		assertArrayEquals(expectedValues, plainArgs.toArray());
	}
}
