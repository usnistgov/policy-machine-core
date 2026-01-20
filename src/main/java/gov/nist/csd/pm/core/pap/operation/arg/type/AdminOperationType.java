package gov.nist.csd.pm.core.pap.operation.arg.type;

import gov.nist.csd.pm.core.pap.operation.AdminOperation;

public final class AdminOperationType extends Type<AdminOperation<?>> {

    @Override
    public AdminOperation<?> cast(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("Object cannot be null");
        }
        if (!(obj instanceof AdminOperation<?> o)) {
            throw new IllegalArgumentException("Cannot cast " + obj.getClass() + " to AdminOperation");
        }

        return o;
    }
}
