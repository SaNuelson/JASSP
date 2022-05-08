package cz.mff.jassp.test_suite_custom;

import cz.mff.jassp.processing.logic.LogicException;
import cz.mff.jassp.option.Option;
import cz.mff.jassp.option.OptionList;
import cz.mff.jassp.parser.ArgumentParser;
import cz.mff.jassp.processing.logic.Rule;
import org.junit.Test;

import java.util.List;

public class RulesTest {

    @Test
    public void simpleRuleAllowsValidArgs() {
        String[] args = new String[] {"-a"};
        OptionList options = new OptionList()  {{
            addOptions(List.of(
                    Option.builder().addShortAlias("a").build(),
                    Option.builder().addShortAlias("b").build()
            ));
            addRules(List.of(
                    Rule.NAnd("a", "b")
            ));
        }};

        ArgumentParser parser = new ArgumentParser();
        parser.parse(options, args);
    }

    @Test(expected = LogicException.class)
    public void simpleRuleBlocksViolatingArgs() {
        String[] args = new String[] {"-a", "-b"};
        OptionList options = new OptionList()  {{
            addOptions(List.of(
                    Option.builder().addShortAlias("a").build(),
                    Option.builder().addShortAlias("b").build()
            ));
            addRules(List.of(
                    Rule.NAnd("a", "b")
            ));
        }};

        ArgumentParser parser = new ArgumentParser();
        parser.parse(options, args);
    }

    @Test
    public void tautologyAllowsAnyValidArgs() {
        String[] args = new String[] {"-a", "-b", "-c"};
        OptionList options = new OptionList()  {{
            addOptions(List.of(
                    Option.builder().addShortAlias("a").build(),
                    Option.builder().addShortAlias("b").build(),
                    Option.builder().addShortAlias("c").build()
            ));
            addRules(List.of(
                    Rule.Or("a", Rule.Not("a")),
                    Rule.Exists("b").Or(Rule.Exists("b").Not()),
                    Rule.Exists("c").Implies(Rule.Exists("c"))
            ));
        }};

        ArgumentParser parser = new ArgumentParser();
        parser.parse(options, args);
    }

    @Test(expected = LogicException.class)
    public void contradictionAlwaysThrows() {
        String[] args = new String[] {"-a", "-b", "-c"};
        OptionList options = new OptionList()  {{
            addOptions(List.of(
                    Option.builder().addShortAlias("a").build(),
                    Option.builder().addShortAlias("b").build(),
                    Option.builder().addShortAlias("c").build()
            ));
            addRules(List.of(
                    Rule.Eq("a", Rule.Not("a"))
            ));
        }};

        ArgumentParser parser = new ArgumentParser();
        parser.parse(options, args);
    }

}
