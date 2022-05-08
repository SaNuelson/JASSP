package cz.mff.jassp.option;

import cz.mff.jassp.parser.ParserException;
import cz.mff.jassp.processing.logic.Rule;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Wrapper for a set of options used during parser.ArgumentParser's parse method
 */
public class OptionList {

    private final List<Option> memory = new ArrayList<>();
    private final List<Rule> rules = new ArrayList<>();

    /**
     * Add a new option to the list.
     * @param option parser.input.Option to add
     */
    public void addOption(Option option) {
        if (memory.contains(option)) {
            throw new ParserException("Trying to add option to option list which is already included.");
        }

        for (String shortAlias : option.getShortAliases()) {
            if (knowsAlias(shortAlias, false)) {
                throw new ParserException("Trying to add option containing short alias (" + shortAlias + ") already known.");
            }
        }

        for (String longAlias: option.getLongAliases()) {
            if (knowsAlias(longAlias, true)) {
                throw new ParserException("Trying to add option containing long alias (" + longAlias + ") already known.");
            }
        }

        if (memory.stream().anyMatch(existingOption -> Objects.equals(existingOption.getHandler(), option.getHandler()))) {
            throw new ParserException("Trying to add option with handler (" + option.getHandler() + "equal to handler of existing option.");
        }

        memory.add(option);
    }

    /**
     * Add a set of new options to the option list.
     * @param options a set of options to add
     */
    public void addOptions(List<Option> options) {
        for (Option option : options) {
            addOption(option);
        }
    }

    /**
     * Get option by its handler
     * @return Option if found, null otherwise
     */
    public Option getOption(String handler) {
        return memory.stream()
                .filter(option -> Objects.equals(option.getHandler(), handler))
                .findFirst()
                .orElse(null);
    }

    /**
     * Get a copy of all options that currently are in OptionList.
     * @return A copy of list of Options
     */
    public List<Option> getAllOptions() {
        return new ArrayList<>(memory);
    }

    /**
     * Get a copy of all rules that currently are in OptionList.
     * @return A copy of list of Rules
     */
    public List<Rule> getAllRules() { return new ArrayList<>(rules); }

    /**
     * Get option by one of its aliases
     * @return Option if found, null otherwise
     */
    public Option findOption(String alias) {
        return memory.stream()
                .filter(option -> option.getShortAliases().contains(alias) || option.getLongAliases().contains(alias))
                .findFirst()
                .orElse(null);
    }

    /**
     * Add custom rule to the group.
     * For built-in rules, see {@link Rule} static methods.
     * @param rule rule to enforce when parsing
     */
    public void addRule(Rule rule) {
        // TODO: newly added rule validation
        rules.add(rule);
    }

    /**
     * Add custom rules to the option list.
     * For built-in rules, see {@link Rule} static methods.
     * @param rules list of new rules to enforce when parsing
     */
    public void addRules(List<Rule> rules) {
        for (Rule rule : rules) {
            addRule(rule);
        }
    }

    /**
     * Checks whether a provided alias is known in some of its options
     * @param alias alias to check for
     * @param longKind whether the provided alias is of long type
     * @return True if an option exists that has such alias defined, false otherwise.
     */
    public boolean knowsAlias(String alias, boolean longKind) {
        if (longKind) {
            return memory.stream().anyMatch(option -> option.getLongAliases().contains(alias));
        }
        else {
            return memory.stream().anyMatch(option -> option.getShortAliases().contains(alias));
        }
    }
}