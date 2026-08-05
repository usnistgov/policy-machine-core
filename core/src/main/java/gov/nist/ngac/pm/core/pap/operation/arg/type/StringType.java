package gov.nist.ngac.pm.core.pap.operation.arg.type;

/**
 * The PML string type. Casting stringifies any non-null value via {@link Object#toString()} rather than
 * requiring it already be a String.
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
