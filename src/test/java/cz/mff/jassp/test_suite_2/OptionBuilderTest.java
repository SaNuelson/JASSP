package cz.mff.jassp.test_suite_2;

import cz.mff.jassp.option.BuilderException;
import cz.mff.jassp.option.Option;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public final class OptionBuilderTest {
    @Test
    public void canAddShortAlias() {
        Option option = Option.builder()
                .setHandler("handler")
                .addShortAlias("x")
                .build();

        assertArrayEquals(new Object[]{"x"}, option.getShortAliases().toArray());
        assertArrayEquals(new Object[]{}, option.getLongAliases().toArray());
    }

    @Test
    public void emptyShortAliasThrows() {
        assertThrows(BuilderException.class,
                () -> Option.builder()
                        .setHandler("handler")
                        .addShortAlias(""));
    }

    @Test
    public void multiCharShortAliasThrows() {
        assertThrows(BuilderException.class,
                () -> Option.builder()
                        .setHandler("handler")
                        .addShortAlias("xx"));
    }

    @Test
    public void duplicateShortAliasThrows() {
        assertThrows(BuilderException.class,
                () -> Option.builder()
                        .setHandler("handler")
                        .addShortAlias("x")
                        .addShortAlias("x"));
    }

    @Test
    public void canAddLongAlias() {
        Option option = Option.builder()
                .setHandler("handler")
                .addLongAlias("xxx")
                .build();

        assertArrayEquals(new Object[]{}, option.getShortAliases().toArray());
        assertArrayEquals(new Object[]{"xxx"}, option.getLongAliases().toArray());
    }

    @Test
    public void emptyLongAliasThrows() {
        assertThrows(BuilderException.class,
                () -> Option.builder()
                        .setHandler("handler")
                        .addLongAlias("")
        );
    }

    @Test
    public void duplicateLongAliasThrows() {
        assertThrows(BuilderException.class,
                () -> Option.builder()
                        .setHandler("handler")
                        .addLongAlias("xxx")
                        .addLongAlias("xxx")
        );
    }

    @Test
    public void handlerInferredFromShortOption() {
        Option option = Option.builder()
                .addShortAlias("x")
                .build();

        assertEquals("x", option.getHandler());
    }

    @Test
    public void handlerInferredFromLongOption() {
        Option option = Option.builder()
                .addLongAlias("xxx")
                .build();

        assertEquals("xxx", option.getHandler());
    }

    @Test
    public void handlerInferredFromShortOptionIfMultipleLongOptions() {
        Option option = Option.builder()
                .addLongAlias("yyy")
                .addShortAlias("x")
                .addLongAlias("zzz")
                .build();

        assertEquals("x", option.getHandler());
    }

    @Test
    public void handlerInferredFromLongOptionIfMultipleShortOptions() {
        Option option = Option.builder()
                .addShortAlias("y")
                .addLongAlias("xxx")
                .addShortAlias("z")
                .build();

        assertEquals("xxx", option.getHandler());
    }

    @Test
    public void longOptionPreferredForHandlerInference() {
        Option option = Option.builder()
                .addLongAlias("xxx")
                .addShortAlias("y")
                .build();

        assertEquals("xxx", option.getHandler());
    }

    @Test
    public void subsequentLongOptionPreferredForHandlerInference() {
        Option option = Option.builder()
                .addShortAlias("y")
                .addLongAlias("xxx")
                .build();

        assertEquals("xxx", option.getHandler());
    }

    @Test
    public void explicitHandlerDisablesInference() {
        Option option = Option.builder()
                .setHandler("handler")
                .addShortAlias("y")
                .addLongAlias("zzz")
                .build();

        assertEquals("handler", option.getHandler());
    }

    @Test
    public void subsequentExplicitHandlerDisablesInference() {
        Option option = Option.builder()
                .addShortAlias("y")
                .addLongAlias("zzz")
                .setHandler("handler")
                .build();

        assertEquals("handler", option.getHandler());
    }

    @Test
    public void optionMustHaveSomeAlias() {
        assertThrows(BuilderException.class,
                () -> Option.builder()
                        .setHandler("handler")
                        .build()
        );
    }

    @Test
    public void addShortAliasesAddsAll() {
        Option option = Option.builder()
                .setHandler("handler")
                .addShortAliases(List.of("x", "y"))
                .build();

        assertArrayEquals(new Object[]{"x", "y"}, option.getShortAliases().toArray());
        assertArrayEquals(new Object[]{}, option.getLongAliases().toArray());
    }

    @Test
    public void addLongAliasesAddsAll() {
        Option option = Option.builder()
                .setHandler("handler")
                .addLongAliases(List.of("xxx", "yyy"))
                .build();

        assertArrayEquals(new Object[]{}, option.getShortAliases().toArray());
        assertArrayEquals(new Object[]{"xxx", "yyy"}, option.getLongAliases().toArray());
    }

    @Test
    public void addShortAliasesChecksDuplicates() {
        assertThrows(BuilderException.class,
                () -> Option.builder()
                        .setHandler("handler")
                        .addShortAliases(List.of("x", "x")));
    }

    @Test
    public void addLongAliasesChecksDuplicates() {
        assertThrows(BuilderException.class,
                () -> Option.builder()
                        .setHandler("handler")
                        .addLongAliases(List.of("xxx", "xxx")));
    }

    @Test
    public void notRequiredByDefault() {
        Option option = Option.builder()
                .setHandler("handler")
                .addShortAlias("x")
                .expectsParameter()
                .build();

        assertFalse(option.isRequired());
    }

    @Test
    public void canSetRequired() {
        Option option = Option.builder()
                .setHandler("handler")
                .addShortAlias("x")
                .expectsParameter()
                .setRequired()
                .build();

        assertTrue(option.isRequired());
    }

    @Test
    public void requiredFlagThrows() {
        assertThrows(BuilderException.class,
                () -> Option.builder()
                        .setHandler("handler")
                        .addShortAlias("x")
                        .setRequired()
                        .build());
    }
}
