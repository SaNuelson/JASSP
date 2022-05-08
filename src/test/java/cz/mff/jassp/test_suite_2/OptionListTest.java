package cz.mff.jassp.test_suite_2;

import cz.mff.jassp.option.OptionList;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import static cz.mff.jassp.test_suite_2.Utils.simpleLongOption;
import static cz.mff.jassp.test_suite_2.Utils.simpleShortOption;

public final class OptionListTest {
    // TODO What exception should be thrown? I suggest inlining this constant after setting it to a more specific class.
    private static final Class<? extends Exception> OPTION_LIST_EXCEPTION = RuntimeException.class;

    @Test
    public void duplicateShortAliasThrows() {
        Assert.assertThrows(OPTION_LIST_EXCEPTION, () -> {
            new OptionList() {{
                addOption(simpleShortOption("handler1", "x"));
                addOption(simpleShortOption("handler2", "x"));
            }};
        });
    }

    @Test
    public void duplicateLongAliasThrows() {
        Assert.assertThrows(OPTION_LIST_EXCEPTION, () -> {
            new OptionList() {{
                addOption(simpleLongOption("handler1", "xxx"));
                addOption(simpleLongOption("handler2", "xxx"));
            }};
        });
    }

    @Test
    public void duplicateHandlerThrows() {
        Assert.assertThrows(OPTION_LIST_EXCEPTION, () -> {
            new OptionList() {{
                addOption(simpleShortOption("handler", "x"));
                addOption(simpleLongOption("handler", "xxx"));
            }};
        });
    }

    @Test
    public void shortAliasesAndHandlersDoNotClash() {
        String thisIsDuplicate = "x";
        new OptionList() {{
            addOption(simpleShortOption("handler", thisIsDuplicate));
            addOption(simpleLongOption(thisIsDuplicate, "xxx"));
        }};
    }

    @Test
    public void longAliasesAndHandlersDoNotClash() {
        String thisIsDuplicate = "xxx";
        new OptionList() {{
            addOption(simpleShortOption(thisIsDuplicate, "x"));
            addOption(simpleLongOption("handler", thisIsDuplicate));
        }};
    }
}
