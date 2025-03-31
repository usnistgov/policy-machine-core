package gov.nist.csd.pm.pap.function.arg.type;

public class BooleanType extends ArgType<Boolean> {

    @Override
    public Boolean cast(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("Object cannot be null");
        }
        if (!(obj instanceof Boolean bool)) {
            throw new IllegalArgumentException("Cannot cast " + obj.getClass() + " to Boolean");
        }

        return bool;
    }
}
