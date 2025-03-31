package gov.nist.csd.pm.pap.function.arg.type;

public class LongType extends ArgType<Long> {

    @Override
    public Long cast(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("Object cannot be null");
        }
        if (!(obj instanceof Long l)) {
            throw new IllegalArgumentException("Cannot cast " + obj.getClass() + " to Long");
        }

        return l;
    }
}
