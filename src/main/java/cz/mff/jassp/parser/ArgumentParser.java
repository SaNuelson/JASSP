package cz.mff.jassp.parser;

import cz.mff.jassp.processing.logic.LogicException;
import cz.mff.jassp.option.Option;
import cz.mff.jassp.option.OptionList;
import cz.mff.jassp.processing.extractor.Extractor;
import cz.mff.jassp.processing.logic.Rule;

import java.util.List;

/**
 * Main class responsible for handling the parsing process of arguments
 */
public class ArgumentParser {

    public static final String plainArgsDelimiter = "--";
    public static final String shortAliasPrefix = "-";
    public static final String longAliasPrefix = "--";

    /**
     * Parse string array using provided option objects.
     *
     * @param options OptionList object populated by option specification
     * @param args String array to parse against the OptionList (e.g., command-line arguments)
     * @return ParsedArgList object containing any parsed values matching the options
     */
    public ParsedArgList parse(OptionList options, String[] args) throws ParserException {

        ParsedArgList parsedArgList = new ParsedArgList();

        int i = 0;

        // Option arguments phase

        while (i < args.length) {
            String token = args[i];

            if (token.equals(plainArgsDelimiter)) {
                i++;
                break;
            }

            // If a plain or an empty string is found, jump to the plain args parsing phase
            if (token.equals("") || isPlainArg(token)) {
                break;
            }

            String optionName = optionToAlias(token);
            Option optionObj = options.findOption(optionName);

            if (optionObj == null) {
                throw new ParserException("An undefined option was found during parsing: " + optionName);
            }

            for (ParsedArgument<?> foundParsedArg : parsedArgList.getParsedArgs()) {
                if (optionObj.getHandler().equals(foundParsedArg.getHandler())) {
                    throw new ParserException("An option was found multiple times during parsing: " + optionName);
                }
            }

            // Check if the option expects a parameter
            Extractor<?> optExtractor = optionObj.getExtractor();

            ParsedArgument<?> newParsedArg;
            if (optExtractor == null) {
                newParsedArg = new ParsedArgument<>(
                        optionObj.getHandler(),
                        token,
                        true,
                        true);
            }
            else {
                // Shift to option's value
                i++;

                if (i == args.length) {
                    throw new ParserException("Expecting parameter for " + optionName + ", found end of args.");
                }

                String rawValue = args[i];

                newParsedArg = new ParsedArgument<>(
                        optionObj.getHandler(),
                        rawValue,
                        optExtractor.parse(rawValue),
                        true);
            }

            parsedArgList.addParsedArg(newParsedArg);
            i++;
        }

        // Plain arguments phase

        while (i < args.length) {
            if (args[i] == null)
                throw new ParserException("Args cannot contain null");

            parsedArgList.addPlainArg(args[i]);
            i++;
        }

        // Post-processing and validation phase

        checkMandatoryArgs(options, parsedArgList.getParsedArgs());

        fillMissingArgs(options, parsedArgList);

        enforceRules(parsedArgList, options.getAllRules());


        return parsedArgList;
    }

    /**
     * Iterates over options and calls their respective toString methods
     *
     * @param options defined options
     */
    public void printHelp(OptionList options) {
        for (Option option : options.getAllOptions()) {
            System.out.println(option.toString());
        }
    }

    /**
     * Check if string can be considered a short option specifier.
     * @return True if string starts with a single hyphen "-", which is followed by a valid short alias.
     */
    private static boolean isValidShortOption(String token) {
        if (token.length() < 2 || !token.startsWith(shortAliasPrefix))
            return false;
        return Option.isValidShortAlias(token.substring(1));
    }

    /**
     * Check if string can be considered a long option specified.
     * @return True if string starts with 2 hypens "--" and is followed by a valid long alias.
     */
    private static boolean isValidLongOption(String token) {
        if (token.length() <= 2 || !token.startsWith(longAliasPrefix))
            return false;
        return Option.isValidLongAlias(token.substring(2));
    }

    private static boolean isPlainArg(String token) {
        return token.charAt(0) != '-';
    }

    /**
     * Try to strip option into an alias (either long or short)
     * @param token incoming option prefixed with "-" or "--"
     * @return stripped alias if token is a valid option
     * @throws ParserException if token is an invalid option
     */
    private static String optionToAlias(String token) {
        if (token.startsWith(longAliasPrefix)) {

            if (!isValidLongOption(token))
                throw new ParserException("Option-like token (" + token + ") is an invalid long option");

            return token.substring(2);
        }

        if (token.startsWith(shortAliasPrefix)) {
            if (!isValidShortOption(token))
                throw new ParserException("Option-like token (" + token + ") is an invalid short option");

            return token.substring(1);
        }

        throw new ParserException("Option-like token (" + token + ") is not option-like at all (this shouldn't occur)");
    }

    /**
     * Iterate over all options and check whether any mandatory ones are missing.
     * @throws ParserException If any mandatory options are not present in parsed options
     */
    private static void checkMandatoryArgs(OptionList options, List<ParsedArgument<?>> parsedOptionArguments) {
        for (Option option : options.getAllOptions()) {
            if (option.isRequired()) {
                boolean found = false;
                for (ParsedArgument<?> parsedOptionArg : parsedOptionArguments) {
                    if (parsedOptionArg.getHandler().equals(option.getHandler())) {
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    throw new ParserException("A mandatory option is missing: " + option.getHandler());
                }
            }
        }
    }

    /**
     * Iterate through all options and if any are missing, in parsed arguments, add an empty parsedArg in their place
     */
    private static void fillMissingArgs(OptionList options, ParsedArgList parsedArgList) {
        for (Option option : options.getAllOptions()) {

            if (parsedArgList.hasArgument(option.getHandler()))
                continue;

            ParsedArgument<?> newParsedArg;

            // flags treated separately
            if (option.getExtractor() == null) {
                newParsedArg = new ParsedArgument<>(
                        option.getHandler(),
                        null,
                        false,
                        false);
            } else {
                newParsedArg = new ParsedArgument<>(
                        option.getHandler(),
                        null,
                        null,
                        false);
            }

            parsedArgList.addParsedArg(newParsedArg);
        }
    }

    /**
     * Check that created parsed argument list holds up to all invariants specified by rules
     * @throws LogicException If any invariant is not satisfied
     */
    private void enforceRules(ParsedArgList parsedArgList, List<Rule> rules) {
        for (Rule rule : rules) {
            if (!rule.holds(parsedArgList))
                throw new LogicException("A specified rule is not satisfied.");
        }
    }

}