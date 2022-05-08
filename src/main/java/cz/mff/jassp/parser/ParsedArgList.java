package cz.mff.jassp.parser;

import cz.mff.jassp.option.OptionList;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A wrapper for individual {@link ParsedArgument}s, is constructed during the {@link ArgumentParser#parse(OptionList, String[])}
 * Similarly to how {@link cz.mff.jassp.option.OptionList} wraps {@link cz.mff.jassp.option.Option}
 */
public class ParsedArgList {
    private final List<ParsedArgument<?>> memory;
    private final List<String> plainArgs;

    ParsedArgList() {
        memory = new ArrayList<>();
        plainArgs = new ArrayList<>();
    }

    ParsedArgList(List<ParsedArgument<?>> parsedOptionArguments, List<String> parsedPlainArguments) {
        memory = parsedOptionArguments;
        plainArgs = parsedPlainArguments;
    }

    //region API

    /**
     * Find a parsed argument with provided handler.
     * @param handler identifier of argument
     * @return ParsedArgument if found, null otherwise.
     */
    public ParsedArgument<?> findArgument(String handler) {
        return memory.stream()
                .filter(arg -> Objects.equals(arg.getHandler(), handler))
                .findFirst()
                .orElse(null);
    }

    /**
     * Check if argument with specified handler was found during parsing.
     * This only applies to optional arguments, as mandatory arguments throw a ParserException during parsing.
     * If returned false, the optional argument is still present and queryable via {@link #findArgument(String)},
     * as the entry contains other information (TODO default value)
     * @param handler handler of argument to find
     * @return True if argument appeared in parsed args, false otherwise
     */
    public boolean isPresent(String handler) {
        var arg = findArgument(handler);
        if (arg == null)
            return false;
        return arg.wasMatched();
    }

    /**
     * Check if a parsed argument with such handler exists
     * @param handler identifier of argument
     * @return True if found, false otherwise.
     */
    public boolean hasArgument(String handler) {
        return findArgument(handler) != null;
    }

    public String getRawValue(String handler) {
        var maybeArgument = findArgument(handler);

        if (maybeArgument != null) {
            return maybeArgument.getRawValue();
        }
        else {
            return null;
        }
    }

    /**
     * Return constructed value of parsed argument with provided handler
     * @param handler identifier of argument
     * @return Value as an object if found, null otherwise
     */
    public Object getValue(String handler) {
        var maybeArgument = findArgument(handler);

        if (maybeArgument != null) {
            return maybeArgument.getValue();
        }
        else {
            return null;
        }
    }

    public List<String> getPlainArgs(){
        return plainArgs;
    }

    //endregion

    //region Package-private manipulation

    List<ParsedArgument<?>> getParsedArgs() { return memory; }

    void addParsedArg(ParsedArgument<?> parsedArg) {
        if (memory.stream().anyMatch(memArg -> memArg.getHandler().equals(parsedArg.getHandler())))
            throw new ParserException("Duplicate parsed argument found for handler " + parsedArg.getHandler());

        memory.add(parsedArg);
    }

    void addPlainArg(String plainArg) {
        plainArgs.add(plainArg);
    }

    //endregion

}
