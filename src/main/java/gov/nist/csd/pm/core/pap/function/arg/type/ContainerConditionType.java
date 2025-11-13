package gov.nist.csd.pm.core.pap.function.arg.type;

import gov.nist.csd.pm.core.common.prohibition.ContainerCondition;

public final class ContainerConditionType extends Type<ContainerCondition> {

    @Override
    public ContainerCondition cast(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("Object cannot be null");
        }
        if (!(obj instanceof ContainerCondition c)) {
            throw new IllegalArgumentException("Cannot cast " + obj.getClass() + " to ContainerCondition");
        }

        return c;
    }
}
