package gov.nist.csd.pm.pap.pml.function.arg;

import gov.nist.csd.pm.pap.function.arg.type.ArgType;
import gov.nist.csd.pm.pap.pml.value.Value;

public class ValueType extends ArgType<Value> {

    @Override
    public Value cast(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("Object cannot be null");
        }
        if (!(obj instanceof Value value)) {
            throw new IllegalArgumentException("Cannot cast " + obj.getClass() + " to Value");
        }

        return value;
    }
}
