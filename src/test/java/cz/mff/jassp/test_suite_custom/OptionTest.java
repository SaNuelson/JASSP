package cz.mff.jassp.test_suite_custom;

import cz.mff.jassp.option.BuilderException;
import cz.mff.jassp.option.Option;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class OptionTest {

    @Test(expected = BuilderException.class)
    public void nonInferrableHandlerBuilderTest() {
        Option.builder().build();
    }

    @Test(expected = BuilderException.class)
    public void ambiguousHandlerBuilderTest() {
        var builder = Option.builder()
                .addLongAliases(List.of("alpha", "beta"))
                .addShortAliases(List.of("a", "b"))
                .build();
    }

    @Test
    public void inferrableLongAliasHandlerBuilderTest() {
        var option = Option.builder()
                .addLongAlias("alpha")
                .addShortAliases(List.of("a", "b"))
                .build();
        assertEquals("alpha", option.getHandler());
    }

    @Test
    public void inferrableShortAliasHandlerBuilderTest() {
        var option = Option.builder()
                .addLongAliases(List.of("alpha", "beta"))
                .addShortAlias("a")
                .build();
        assertEquals("a", option.getHandler());
    }

    @Test(expected = BuilderException.class)
    public void emptyOptionTest() {
        var option = Option.builder()
                .setHandler("option")
                .build();
    }

    @Test
    public void filledOptionTest() {
        var sampleDescription = "sample description";
        var sampleShortAliases = Arrays.asList("a", "b", "c");
        var sampleAdditionalShortAlias = "d";
        var sampleLongAliases = Arrays.asList("alpha", "beta", "gamma");
        var sampleAdditionalLongAlias = "delta";

        var option = Option.builder()
                .setDescription(sampleDescription)
                .addShortAliases(sampleShortAliases)
                .addShortAlias(sampleAdditionalShortAlias)
                .addLongAliases(sampleLongAliases)
                .addLongAlias(sampleAdditionalLongAlias)
                .setHandler("option")
                .build();

        assertNotSame(option.getLongAliases(), sampleLongAliases);
        assertTrue(option.getShortAliases().containsAll(sampleShortAliases));
        assertTrue(option.getShortAliases().contains(sampleAdditionalShortAlias));

        assertNotSame(option.getShortAliases(), sampleShortAliases);
        assertTrue(option.getLongAliases().containsAll(sampleLongAliases));
        assertTrue(option.getLongAliases().contains(sampleAdditionalLongAlias));

        // strings can be the same reference as they are immutable
        assertEquals(sampleDescription, option.getDescription());
    }

    @Test
    public void invalidAliasesTest() {
        assertThrows(BuilderException.class, () -> Option.builder().addShortAlias("8"));
        assertThrows(BuilderException.class, () -> Option.builder().addShortAlias("-"));
        assertThrows(BuilderException.class, () -> Option.builder().addShortAlias("aa"));
        assertThrows(BuilderException.class, () -> Option.builder().addShortAlias(""));

        assertThrows(BuilderException.class, () -> Option.builder().addLongAlias("al-pha"));
        assertThrows(BuilderException.class, () -> Option.builder().addLongAlias("ga mma"));
        assertThrows(BuilderException.class, () -> Option.builder().addLongAlias(""));
    }

    @Test
    public void afterBuildTest() {
        var builder = Option.builder()
                .addLongAlias("alpha")
                .addShortAlias("a")
                .setDescription("sample description");

        var option = builder.build();

        assertThrows(BuilderException.class, builder::setRequired);
        assertFalse(option.isRequired());
    }
}