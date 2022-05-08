package cz.mff.jassp.processing.logic;

import cz.mff.jassp.parser.ParsedArgList;
import cz.mff.jassp.parser.ParsedArgument;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AtomicRule extends Rule {
    private final List<String> optHandlers;
    private final Function<Map<String, ? extends ParsedArgument<?>>, Boolean> evaluator;

    public AtomicRule(List<String> optionHandlers, Function<Map<String, ? extends ParsedArgument<?>>, Boolean> checkFunction) {
        optHandlers = optionHandlers;
        evaluator = checkFunction;
    }

    public boolean holds(ParsedArgList target) {
        var targetArgs = optHandlers
                .stream()
                .map(target::findArgument)
                .collect(Collectors.toMap(ParsedArgument::getHandler, Function.identity()));

        return evaluator.apply(targetArgs);
    }

}