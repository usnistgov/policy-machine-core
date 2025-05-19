package gov.nist.csd.pm.pap.function.op.operation;

import gov.nist.csd.pm.pap.function.arg.type.Type;
import gov.nist.csd.pm.pap.function.op.Operation;

final class OperationType extends Type<Operation<?, ?>> {

    @Override
    public Operation<?, ?> cast(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("Object cannot be null");
        }
        if (!(obj instanceof Operation<?, ?> op)) {
            throw new IllegalArgumentException("Cannot cast " + obj.getClass() + " to Operation");
        }

        return op;
    }

    @Override
    public Class<Operation<?, ?>> getExpectedClass() {
        return (Class<Operation<?, ?>>)(Class<?>) Operation.class;
    }
}
