package cz.mff.jassp.test_suite_custom;

import cz.mff.jassp.processing.extractor.ExtractorException;
import cz.mff.jassp.processing.logic.LogicException;
import cz.mff.jassp.option.Option;
import cz.mff.jassp.option.OptionList;
import cz.mff.jassp.parser.ArgumentParser;
import cz.mff.jassp.parser.ParsedArgList;
import cz.mff.jassp.processing.extractor.Extractor;
import cz.mff.jassp.processing.logic.AtomicRule;
import cz.mff.jassp.processing.logic.Rule;
import org.junit.Test;

import java.util.List;
import java.util.Objects;

import static org.junit.Assert.*;

record Person(String name, int age) {

    public static boolean isValidName(String name) {
        return name.length() > 5;
    }

    public static boolean isValidAge(int age) {
        return age > 0;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;

        if (!(o instanceof Person other))
            return false;

        return Objects.equals(this.name, other.name) && this.age == other.age;
    }
}

class PersonExtractor extends Extractor<Person>{
    @Override
    public boolean validate(String match) {
        String[] split = match.split(",");
        if (split.length != 2)
            return false;

        if (!Person.isValidName(split[0]))
            return false;

        int age;
        try {
            age = Integer.parseInt(split[1]);
        }
        catch(NumberFormatException e) {
            return false;
        }

        return Person.isValidAge(age);
    }

    @Override
    public Person parse(String match) throws ExtractorException {
        String[] split = match.split(",");
        return new Person(split[0], Integer.parseInt(split[1]));
    }
}


public class ReadmeTest {

    ParsedArgList wrapOptionAndParse(Option option, String[] args) {
        OptionList group = new OptionList();
        group.addOption(option);

        ArgumentParser parser = new ArgumentParser();
        return parser.parse(group, args);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void customExtractorTest() {
        String[] args = new String[]{
                "--people",
                "JackBlack,52;JimCarrey,60;BradPitt,58"
        };

        List<Person> expected =  List.of(
                new Person("JackBlack", 52),
                new Person("JimCarrey", 60),
                new Person("BradPitt", 58)
        );

        OptionList options = new OptionList();
        options.addOption(Option.builder()
                .addLongAlias("people")
                .setExtractor(Extractor.List(";", new PersonExtractor()))
                .build()
        );

        ArgumentParser parser = new ArgumentParser();
        ParsedArgList parsedArgs = parser.parse(options, args);

        List<Person> actual = (List<Person>) parsedArgs.getValue("people");

        assertEquals(expected.size(), actual.size());
        for (var expectedPerson : expected) {
            assertTrue(actual.stream().anyMatch(expectedPerson::equals));
        }
    }

    @Test
    public void customRuleTest() {
        OptionList options = new OptionList();
        options.addOptions(List.of(
                Option.builder()
                        .addLongAlias("alpha")
                        .setExtractor(Extractor.Integer())
                        .build(),
                Option.builder()
                        .addLongAlias("beta")
                        .setExtractor(Extractor.Integer())
                        .build()
        ));

        AtomicRule alphaGreaterThanBeta = new AtomicRule(
                List.of("alpha", "beta"),
                args -> {
                    int alpha = (int) args.get("alpha").getValue();
                    int beta = (int) args.get("beta").getValue();
                    return alpha > beta;
                }
        );

        options.addRule(alphaGreaterThanBeta);
        // ...
    }

    @Test
    public void optionExampleTest() {
        Option exampleOption = Option.builder()
                .addShortAlias("i")
                .addLongAlias("input")
                .setDescription("Path to the input file")
                .setRequired()
                .expectsParameter()
                .build();

        String[] args1 = new String[]{"-i", "input.txt"};
        ParsedArgList parsedArgs1 = wrapOptionAndParse(exampleOption, args1);
        assertEquals("input.txt", parsedArgs1.getValue("input"));

        String[] args2 = new String[]{"--input", "input.txt"};
        ParsedArgList parsedArgs2 = wrapOptionAndParse(exampleOption, args2);
        assertEquals("input.txt", parsedArgs2.getValue("input"));
    }

    @Test
    public void extractorExampleTest() {
        Option seedOption = Option.builder()
                .addShortAlias("s")
                .addLongAlias("seed")
                .setExtractor(Extractor.Integer())
                .build();

        String[] args = new String[]{"--seed", "42"};
        ParsedArgList parsedArgs = wrapOptionAndParse(seedOption, args);
        assertEquals(42, (int) parsedArgs.getValue("seed"));
    }

    @Test
    public void complexExtractorExampleTest() {
        Option rangeListOption = Option.builder()
                .addShortAlias("r")
                .setExtractor(
                        Extractor.List(",",
                                Extractor.List("-",
                                        Extractor.Integer())))
                .build();

        String[] args = new String[]{"-r", "1-44,2-55,3-66"};
        ParsedArgList parsedArgs = wrapOptionAndParse(rangeListOption, args);
        List<List<Integer>> expected = List.of(
                List.of(1, 44),
                List.of(2, 55),
                List.of(3, 66));

        assertTrue(parsedArgs.hasArgument("r"));
        assertEquals(expected, parsedArgs.getValue("r"));
    }

    @Test
    public void exampleRulesTest() {
        OptionList group = new OptionList();
        group.addOptions(List.of(
                Option.builder()
                        .addLongAlias("release")
                        .build(),
                Option.builder()
                        .addLongAlias("debug")
                        .build(),
                Option.builder()
                        .addLongAlias("level")
                        .setExtractor(Extractor.Integer())
                        .build()
        ));

        group.addRules(List.of(
                Rule.Xor("release", "debug"),
                Rule.Implies("level", "debug")
        ));

        String[] correctArgs = new String[]{"--debug", "--level", "3"};
        String[] incorrectArgs1 = new String[]{"--debug", "--release"};
        String[] incorrectArgs2 = new String[]{"--level", "2"};

        ArgumentParser parser = new ArgumentParser();

        ParsedArgList parsedArgs = parser.parse(group, correctArgs);
        assertTrue((boolean) parsedArgs.getValue("debug"));
        assertFalse((boolean) parsedArgs.getValue("release"));
        assertEquals(3, parsedArgs.getValue("level"));

        assertThrows(LogicException.class, () -> parser.parse(group, incorrectArgs1));
        assertThrows(LogicException.class, () -> parser.parse(group, incorrectArgs2));
    }

}
