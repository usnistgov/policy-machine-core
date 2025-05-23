package gov.nist.csd.pm.core.pap.function.arg.type;

public final class StringType extends Type<String> {

    @Override
    public String cast(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("Object cannot be null");
        }
        return obj.toString();
    }

}
