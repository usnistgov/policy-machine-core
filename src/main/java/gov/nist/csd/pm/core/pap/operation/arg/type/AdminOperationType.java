package gov.nist.csd.pm.core.pap.operation.arg.type;

import gov.nist.csd.pm.core.pap.operation.Operation;

public final class AdminOperationType extends Type<Operation<?>> {

    @Override
    public Operation<?> cast(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("Object cannot be null");
        }
        if (!(obj instanceof Operation<?> o)) {
            throw new IllegalArgumentException("Cannot cast " + obj.getClass() + " to Operation");
        }

        return o;
    }
}
