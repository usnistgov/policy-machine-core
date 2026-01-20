package gov.nist.csd.pm.core.pap.operation.arg.type;

import gov.nist.csd.pm.core.pap.operation.BasicFunction;

public final class BasicFunctionType extends Type<BasicFunction<?>> {

    @Override
    public BasicFunction<?> cast(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("Object cannot be null");
        }
        if (!(obj instanceof BasicFunction<?> o)) {
            throw new IllegalArgumentException("Cannot cast " + obj.getClass() + " to BasicFunction");
        }

        return o;
    }
}
