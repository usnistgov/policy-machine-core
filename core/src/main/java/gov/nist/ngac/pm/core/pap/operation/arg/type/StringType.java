package gov.nist.ngac.pm.core.pap.operation.arg.type;

/**
 * Supported type for String.
 */
public final class StringType extends Type<String> {

    @Override
    public String cast(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("Object cannot be null");
        }
        return obj.toString();
    }

}
