package gov.nist.csd.pm.pap.function.arg.type;

public final class StringType extends ArgType<String> {

    @Override
    public String cast(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("Object cannot be null");
        }
        return obj.toString();
    }

    @Override
    public Class<String> getExpectedClass() {
        return String.class;
    }
}
