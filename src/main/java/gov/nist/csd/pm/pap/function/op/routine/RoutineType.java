package gov.nist.csd.pm.pap.function.op.routine;

import gov.nist.csd.pm.pap.function.arg.type.Type;
import gov.nist.csd.pm.pap.function.routine.Routine;

final class RoutineType extends Type<Routine<?, ?>> {

    @Override
    public Routine<?, ?> cast(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("Object cannot be null");
        }
        if (!(obj instanceof Routine<?, ?> op)) {
            throw new IllegalArgumentException("Cannot cast " + obj.getClass() + " to Routine");
        }

        return op;
    }

    @Override
    public Class<Routine<?, ?>> getExpectedClass() {
        return (Class<Routine<?, ?>>)(Class<?>) Routine.class;

    }
}
