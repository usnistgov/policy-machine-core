package gov.nist.ngac.pm.core.pap.operation.arg.type;

import gov.nist.ngac.pm.core.pap.operation.Routine;

/**
 * Supported type for Routine.
 */
public final class RoutineType extends Type<Routine<?>> {

    @Override
    public Routine<?> cast(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("Object cannot be null");
        }
        if (!(obj instanceof Routine<?> r)) {
            throw new IllegalArgumentException("Cannot cast " + obj.getClass() + " to Routine");
        }

        return r;
    }
}
