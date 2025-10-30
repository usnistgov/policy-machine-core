package gov.nist.csd.pm.core.pap.function.arg.type;

import gov.nist.csd.pm.core.pap.function.op.Operation;

public final class OperationType extends Type<Operation<?>> {

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
