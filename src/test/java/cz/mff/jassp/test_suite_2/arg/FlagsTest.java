package cz.mff.jassp.test_suite_2.arg;

import cz.mff.jassp.option.Option;
import cz.mff.jassp.option.OptionList;
import cz.mff.jassp.parser.ParsedArgList;
import org.junit.Test;

import static cz.mff.jassp.test_suite_2.Utils.arrayOf;
import static cz.mff.jassp.test_suite_2.Utils.parse;
import static org.junit.Assert.*;

public final class FlagsTest {

    @Test
    public void omittedFlagShortOptionIsNotPresent() {
        OptionList optionList = new OptionList() {{
            addOption(Option.builder().addShortAlias("x").build());
        }};

        ParsedArgList result = parse(optionList, "lorem", "ipsum");

        assertFalse(result.isPresent("x"));
    }

    @Test
    public void providedFlagShortOptionIsPresent() {
        OptionList optionList = new OptionList() {{
            addOption(Option.builder().addShortAlias("x").build());
        }};

        ParsedArgList result = parse(optionList, "-x", "lorem", "ipsum");

        assertTrue(result.isPresent("x"));
    }

    @Test
    public void omittedFlagLongOptionIsNotPresent() {
        OptionList optionList = new OptionList() {{
            addOption(Option.builder().addLongAlias("xxx").build());
        }};

        ParsedArgList result = parse(optionList, "lorem", "ipsum");

        assertFalse(result.isPresent("xxx"));
    }

    @Test
    public void providedFlagLongOptionIsPresent() {
        OptionList optionList = new OptionList() {{
            addOption(Option.builder().addLongAlias("xxx").build());
        }};

        ParsedArgList result = parse(optionList, "--xxx", "lorem", "ipsum");

        assertTrue(result.isPresent("xxx"));
    }

    @Test
    public void flagsRemovedFromPlainArgs() {
        OptionList optionList = new OptionList() {{
            addOption(Option.builder().addShortAlias("x").build());
            addOption(Option.builder().addLongAlias("xxx").build());
        }};

        ParsedArgList result = parse(optionList, "lorem", "-x", "ipsum", "--xxx", "dolor");

        assertArrayEquals(arrayOf("lorem", "-x", "ipsum", "--xxx", "dolor"), result.getPlainArgs().toArray());
    }

    @Test
    public void flagsIgnoredAfterDelimiter() {
        OptionList optionList = new OptionList() {{
            addOption(Option.builder().addShortAlias("x").build());
            addOption(Option.builder().addLongAlias("xxx").build());
            addOption(Option.builder().addLongAlias("output").build());
        }};

        ParsedArgList result = parse(optionList, "--output", "--", "-x", "ipsum", "--xxx", "dolor");

        assertFalse(result.isPresent("x"));
        assertFalse(result.isPresent("xxx"));
    }

    @Test
    public void flagsNotRemovedFromPlainArgsAfterDelimiter() {
        OptionList optionList = new OptionList() {{
            addOption(Option.builder().addShortAlias("x").build());
            addOption(Option.builder().addLongAlias("xxx").build());
            addOption(Option.builder().addLongAlias("output").build());
        }};

        ParsedArgList result = parse(optionList, "--output", "--", "-x", "ipsum", "--xxx", "dolor");

        assertArrayEquals(arrayOf("-x", "ipsum", "--xxx", "dolor"), result.getPlainArgs().toArray());
    }
}
