package gov.nist.csd.pm.core.pap.function.arg.type;

import gov.nist.csd.pm.core.pap.obligation.Rule;

public final class RuleType extends Type<Rule> {

    @Override
    public Rule cast(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("Object cannot be null");
        }
        if (!(obj instanceof Rule r)) {
            throw new IllegalArgumentException("Cannot cast " + obj.getClass() + " to Rule");
        }

        return r;
    }
}
