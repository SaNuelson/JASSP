package cz.mff.jassp.test_suite_2;

import cz.mff.jassp.option.Option;
import cz.mff.jassp.option.OptionList;
import cz.mff.jassp.parser.ArgumentParser;
import cz.mff.jassp.parser.ParsedArgList;

public final class Utils {
    private Utils() throws Throwable {
        throw new UnsupportedOperationException();
    }

    // Slightly prettier syntax for new Object[]{...}.
    public static Object[] arrayOf(Object... elems) {
        return elems;
    }

    // Convenience method to avoid new ArgumentParser() and new String[]{...} everywhere.
    public static ParsedArgList parse(OptionList optionList, String... args) {
        return new ArgumentParser().parse(optionList, args);
    }

    public static Option simpleShortOption(String handler, String name) {
        return Option.builder()
                .setHandler(handler)
                .addShortAlias(name)
                .build();
    }

    public static Option simpleLongOption(String handler, String name) {
        return Option.builder()
                .setHandler(handler)
                .addLongAlias(name)
                .build();
    }
}
