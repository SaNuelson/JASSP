package cz.mff.jassp.processing.logic;


import cz.mff.jassp.parser.ParsedArgList;
import cz.mff.jassp.parser.ParsedArgument;

import java.util.List;

/**
 * A class which enables enforcing rules on top of parsed results.
 * It is essentially a boolean expression tree which takes two additional Rules and joins them via logical operator.
 * The leafs are then atomic rules, which evaluate a boolean function on top of specified set of options.
 */
public abstract class Rule {

    public abstract boolean holds(ParsedArgList target);

    //region Built-in shorthand joiners
    public Rule Not() { return new NotRule(this); }

    public Rule Xor(Rule other) { return new BinaryRule(this, other, BinaryRule.Operator.XOR); }
    public Rule Xor(String other) { return Xor(Rule.Exists(other)); }

    public Rule Eq(Rule other) { return new BinaryRule(this, other, BinaryRule.Operator.EQ); }
    public Rule Eq(String other) { return Eq(this, Rule.Exists(other)); }

    public Rule NAnd(Rule other) { return new BinaryRule(this, other, BinaryRule.Operator.NAND); }
    public Rule NAnd(String other) { return NAnd(this, Rule.Exists(other)); }

    public Rule Implies(Rule other) { return new BinaryRule(this, other, BinaryRule.Operator.IMPLIES); }
    public Rule Implies(String other) { return Implies(this, Rule.Exists(other)); }

    public Rule ImpliedBy(Rule other) { return new BinaryRule(this, other, BinaryRule.Operator.IMPLIEDBY); }
    public Rule ImpliedBy(String other) { return ImpliedBy(this, Rule.Exists(other)); }

    public Rule Or(Rule other) { return new BinaryRule(this, other, BinaryRule.Operator.OR); }
    public Rule Or(String other) { return Or(this, Rule.Exists(other)); }
    //endregion

    //region Built-in shorthand factories
    public static Rule Exists(String handler) {
        return new AtomicRule(List.of(handler),
                map -> map.values().stream().allMatch(ParsedArgument::wasMatched));
    }

    public static Rule Not(Rule subrule) { return new NotRule(subrule); }
    public static Rule Not(String subrule) { return Rule.Not(Rule.Exists(subrule)); }

    public static Rule Xor(Rule left, Rule right) { return new BinaryRule(left, right, BinaryRule.Operator.XOR); }
    public static Rule Xor(String left, Rule right) { return Xor(Rule.Exists(left), right); }
    public static Rule Xor(Rule left, String right) { return Xor(left, Rule.Exists(right)); }
    public static Rule Xor(String left, String right) { return Xor(Rule.Exists(left), Rule.Exists(right)); }

    public static Rule Eq(Rule left, Rule right) { return new BinaryRule(left, right, BinaryRule.Operator.EQ); }
    public static Rule Eq(String left, Rule right) { return Eq(Rule.Exists(left), right); }
    public static Rule Eq(Rule left, String right) { return Eq(left, Rule.Exists(right)); }
    public static Rule Eq(String left, String right) { return Eq(Rule.Exists(left), Rule.Exists(right)); }

    public static Rule NAnd(Rule left, Rule right) { return new BinaryRule(left, right, BinaryRule.Operator.NAND); }
    public static Rule NAnd(String left, Rule right) { return NAnd(Rule.Exists(left), right); }
    public static Rule NAnd(Rule left, String right) { return NAnd(left, Rule.Exists(right)); }
    public static Rule NAnd(String left, String right) { return NAnd(Rule.Exists(left), Rule.Exists(right)); }

    public static Rule Implies(Rule left, Rule right) { return new BinaryRule(left, right, BinaryRule.Operator.IMPLIES); }
    public static Rule Implies(String left, Rule right) { return Implies(Rule.Exists(left), right); }
    public static Rule Implies(Rule left, String right) { return Implies(left, Rule.Exists(right)); }
    public static Rule Implies(String left, String right) { return Implies(Rule.Exists(left), Rule.Exists(right)); }

    public static Rule ImpliedBy(Rule left, Rule right) { return new BinaryRule(left, right, BinaryRule.Operator.IMPLIEDBY); }
    public static Rule ImpliedBy(String left, Rule right) { return ImpliedBy(Rule.Exists(left), right); }
    public static Rule ImpliedBy(Rule left, String right) { return ImpliedBy(left, Rule.Exists(right)); }
    public static Rule ImpliedBy(String left, String right) { return ImpliedBy(Rule.Exists(left), Rule.Exists(right)); }

    public static Rule Or(Rule left, Rule right) { return new BinaryRule(left, right, BinaryRule.Operator.OR); }
    public static Rule Or(String left, Rule right) { return Or(Rule.Exists(left), right); }
    public static Rule Or(Rule left, String right) { return Or(left, Rule.Exists(right)); }
    public static Rule Or(String left, String right) { return Or(Rule.Exists(left), Rule.Exists(right)); }
    //endregion
}