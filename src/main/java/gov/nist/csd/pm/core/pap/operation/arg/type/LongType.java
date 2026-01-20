package gov.nist.csd.pm.core.pap.operation.arg.type;

public final class LongType extends Type<Long> {

    @Override
    public Long cast(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("Object cannot be null");
        }
        if (obj instanceof Number number) {
            return number.longValue();
        }
        throw new IllegalArgumentException("cannot convert " + obj.getClass() + " to Long");
    }

}
