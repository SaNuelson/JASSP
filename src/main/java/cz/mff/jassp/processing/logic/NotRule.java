package cz.mff.jassp.processing.logic;

import cz.mff.jassp.parser.ParsedArgList;

public class NotRule extends Rule{

    private final Rule subrule;

    public NotRule(Rule subrule) {
        this.subrule = subrule;
    }

    @Override
    public boolean holds(ParsedArgList target) {
        return !subrule.holds(target);
    }
}
