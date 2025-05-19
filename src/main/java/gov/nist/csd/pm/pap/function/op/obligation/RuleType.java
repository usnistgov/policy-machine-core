package gov.nist.csd.pm.pap.function.op.obligation;

import gov.nist.csd.pm.pap.function.arg.type.Type;
import gov.nist.csd.pm.pap.obligation.Rule;

class RuleType extends Type<Rule> {

    @Override
    public Rule cast(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("Object cannot be null");
        }
        if (!(obj instanceof Rule rule)) {
            throw new IllegalArgumentException("Cannot cast " + obj.getClass() + " to Rule");
        }

        return rule;
    }

    @Override
    public Class<Rule> getExpectedClass() {
        return Rule.class;
    }
}
