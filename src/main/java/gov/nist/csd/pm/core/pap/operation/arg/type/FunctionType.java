package gov.nist.csd.pm.core.pap.operation.arg.type;

import gov.nist.csd.pm.core.pap.operation.Function;

public final class FunctionType extends Type<Function<?>> {

    @Override
    public Function<?> cast(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("Object cannot be null");
        }
        if (!(obj instanceof Function<?> o)) {
            throw new IllegalArgumentException("Cannot cast " + obj.getClass() + " to Function");
        }

        return o;
    }
}
