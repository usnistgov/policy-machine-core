package gov.nist.csd.pm.pap.function.arg.type;

import gov.nist.csd.pm.pap.function.routine.Routine;

public final class RoutineType extends ArgType<Routine<?, ?>> {

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
