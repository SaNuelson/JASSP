package cz.mff.jassp.processing.logic;

import cz.mff.jassp.parser.ParsedArgList;

public class BinaryRule extends Rule {
    public enum Operator {EQ, XOR, NAND, IMPLIES, IMPLIEDBY, OR}

    private final Rule left;
    private final Rule right;
    private final Operator op;

    public BinaryRule(Rule leftRule, Rule rightRule, Operator operator) {
        left = leftRule;
        right = rightRule;
        op = operator;
    }

    @Override
    public boolean holds(ParsedArgList target) {
        return switch (op) {
            case OR -> left.holds(target) || right.holds(target);
            case IMPLIES -> !left.holds(target) || right.holds(target);
            case EQ -> left.holds(target) == right.holds(target);
            case XOR -> left.holds(target) != right.holds(target);
            case NAND -> !(left.holds(target) && right.holds(target));
            case IMPLIEDBY -> left.holds(target) || !right.holds(target);
        };
    }
}
