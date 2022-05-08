package cz.mff.jassp.test_suite_2;

import cz.mff.jassp.option.Option;
import org.junit.Ignore;
import org.junit.Test;

import static cz.mff.jassp.test_suite_2.Utils.*;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertThrows;

public final class OptionImmutabilityTest {
    @Test
    public void longAliasesAreCopied() {
        Option option = simpleLongOption("handler", "xxx");

        option.getLongAliases().add("yyy");

        assertArrayEquals(arrayOf("xxx"), option.getLongAliases().toArray());
    }

    @Test
    public void shortAliasesAreCopied() {
        Option option = simpleShortOption("handler", "x");

        option.getShortAliases().add("y");

        assertArrayEquals(arrayOf("x"), option.getShortAliases().toArray());
    }
}
