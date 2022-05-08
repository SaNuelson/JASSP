package cz.mff.jassp.option;

import cz.mff.jassp.parser.ParserException;
import cz.mff.jassp.processing.extractor.Extractor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Option class used to define parsing of specific part of command-line arguments.
 * It is created only via {@link OptionBuilder}.
 * Once created, it is immutable.
 */
public class Option {

    /**
     * Whether this option is required to be present during parsing.
     * If so, a {@link ParserException} will be thrown if it's missing.
     */
    boolean required = false;
    public boolean isRequired() { return required; }

    /**
     * (Long) literals for setting this option in command-line.
     * Matches literals in command line prefixed with "--".
     * For example long alias "opt" matches command-line option "--opt".
     */
    final ArrayList<String> longAliases = new ArrayList<>();
    public ArrayList<String> getLongAliases() { return new ArrayList<>(longAliases); }

    /**
     * (Short) literals for setting this option in command-line.
     * Matches literals in command line prefixed with "-".
     * For example short alias "a" matches command-line option "-a".
     */
    final ArrayList<String> shortAliases = new ArrayList<>();
    public List<String> getShortAliases() { return new ArrayList<>(shortAliases); }

    /**
     * Handler used to look up argument once parsing is complete.
     */
    String handler;
    public String getHandler() { return handler; }

    /**
     * Short description of the options meaning and usage.
     */
    String description = null;
    public String getDescription() { return description; }

    /**
     * Validator function used to test matched string as well as converting it into desired type.
     */
    Extractor<?> extractor = null;
    public Extractor<?> getExtractor() { return extractor; }

    /**
     * Create new {@link OptionBuilder} which can be used to create a configured {@link Option} instance.
     * @return a new builder instance.
     */
    public static OptionBuilder builder() { return new OptionBuilder(); }

    /**
     * Creates a string which is used to project Option's info to the user.
     *
     * @return A string containing information about an Option.
     */
    @Override
    public String toString() {
        StringBuilder optSb = new StringBuilder();

        for (String shortAlias: shortAliases) {
            optSb.append("-")
                    .append(shortAlias)
                    .append(", ");
        }

        for (String longAlias: longAliases) {
            optSb.append("--")
                    .append(longAlias)
                    .append(", ");
        }

        optSb.append("\n")
                .append("    Required: ")
                .append(isRequired())
                .append(",\n")
                .append("    ")
                .append(Objects.requireNonNullElse(description, "No description."));

        return optSb.toString();
    }

    /**
     * Check if string can be considered a short alias.
     * @return True if string is a single alphabetic character
     */
    public static boolean isValidShortAlias(String alias) {
        return alias.matches("^[a-zA-Z]$");
    }

    /**
     * Check if string can be considered a long alias.
     * @return True if string has at least 2 characters, the first being alphabetic, other being alphanumeric.
     */
    public static boolean isValidLongAlias(String alias) {
        return alias.matches("^[a-zA-Z][a-zA-Z0-9]+$");
    }

}
