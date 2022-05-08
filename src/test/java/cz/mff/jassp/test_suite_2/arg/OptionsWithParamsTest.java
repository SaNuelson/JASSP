package cz.mff.jassp.test_suite_2.arg;

import cz.mff.jassp.parser.ParserException;
import cz.mff.jassp.option.Option;
import cz.mff.jassp.option.OptionList;
import cz.mff.jassp.parser.ParsedArgList;
import cz.mff.jassp.processing.extractor.Extractor;
import org.junit.Test;

import static cz.mff.jassp.test_suite_2.Utils.parse;
import static org.junit.Assert.*;

public final class OptionsWithParamsTest {

    @Test
    public void shortOptionCanHaveSeparateParam() {
        OptionList optionList = new OptionList() {{
            addOption(Option.builder().addShortAlias("x").expectsParameter().build());
        }};

        ParsedArgList result = parse(optionList, "-x", "lorem", "ipsum");

        assertEquals("lorem", result.getValue("x"));
    }

    @Test
    public void longOptionCanHaveSeparateParam() {
        OptionList optionList = new OptionList() {{
            addOption(Option.builder().addLongAlias("xxx").expectsParameter().build());
        }};

        ParsedArgList result = parse(optionList, "--xxx", "ipsum", "dolor");

        assertEquals("ipsum", result.getValue("xxx"));
    }

    @Test
    public void shortOptionWithMissingParamThrows() {
        OptionList optionList = new OptionList() {{
            addOption(Option.builder().addShortAlias("x").expectsParameter().build());
        }};

        assertThrows(ParserException.class, () -> parse(optionList, "-x"));
    }

    @Test
    public void longOptionWithMissingParamThrows() {
        OptionList optionList = new OptionList() {{
            addOption(Option.builder().addLongAlias("xxx").expectsParameter().build());
        }};

        assertThrows(ParserException.class, () -> parse(optionList, "--xxx"));
    }

    @Test
    public void shortOptionMultipleTimesThrows() {
        OptionList optionList = new OptionList() {{
            addOption(Option.builder().addShortAlias("x").expectsParameter().build());
        }};

        assertThrows(ParserException.class, () -> parse(optionList, "-x param -x param"));
    }

    @Test
    public void longOptionMultipleTimesThrows() {
        OptionList optionList = new OptionList() {{
            addOption(Option.builder().addLongAlias("xxx").expectsParameter().build());
        }};

        assertThrows(ParserException.class, () -> parse(optionList, "--xxx param --xxx param"));
    }

    @Test
    public void paramIsParsedUsingExtractor() {
        OptionList optionList = new OptionList() {{
            addOption(Option.builder().addShortAlias("x").setExtractor(Extractor.Integer()).build());
        }};

        ParsedArgList result = parse(optionList, "-x", "42", "dolor");

        assertEquals(42, result.getValue("x"));
    }

    @Test
    public void nonRequiredParamMayBeOmitted() {
        OptionList optionList = new OptionList() {{
            addOption(Option.builder().addShortAlias("x").expectsParameter().build());
        }};

        parse(optionList, "lorem", "dolor");
    }

    @Test
    public void requiredParamMustBeProvided() {
        OptionList optionList = new OptionList() {{
            addOption(Option.builder().addShortAlias("x").expectsParameter().setRequired().build());
        }};

        assertThrows(ParserException.class, () -> parse(optionList, "lorem", "dolor"));
    }
}
