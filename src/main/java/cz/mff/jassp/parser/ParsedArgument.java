package cz.mff.jassp.parser;

import cz.mff.jassp.option.Option;

public class ParsedArgument<T> {
    private final String handler;
    private final T value;
    private final String rawValue;
    private final boolean matched;


    public ParsedArgument(String handler, String rawValue, T value) {
        this(handler, rawValue, value, true);
    }

    public ParsedArgument(String handler, String rawValue, T value, boolean matched) {
        this.handler = handler;
        this.value = value;
        this.rawValue = rawValue;
        this.matched = matched;
    }

    public boolean wasMatched() { return matched; }
    public T getValue() { return value; }
    public String getRawValue() { return rawValue; }
    public String getHandler() { return handler; }
}
