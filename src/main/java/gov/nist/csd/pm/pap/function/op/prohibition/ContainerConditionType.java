package gov.nist.csd.pm.pap.function.op.prohibition;

import gov.nist.csd.pm.common.prohibition.ContainerCondition;
import gov.nist.csd.pm.pap.function.arg.type.ArgType;

class ContainerConditionType extends ArgType<ContainerCondition> {

    @Override
    public ContainerCondition cast(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("Object cannot be null");
        }
        if (!(obj instanceof ContainerCondition containerCondition)) {
            throw new IllegalArgumentException("Cannot cast " + obj.getClass() + " to ContainerCondition");
        }

        return containerCondition;
    }
}
