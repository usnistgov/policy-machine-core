package gov.nist.ngac.pm.core.pap.operation.arg.type;

/**
 * Supported type for Boolean.
 */
public final class BooleanType extends Type<Boolean> {

    @Override
    public Boolean cast(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("Object cannot be null");
        }
        if (!(obj instanceof Boolean b)) {
            throw new IllegalArgumentException("Cannot cast " + obj.getClass() + " to Boolean");
        }

        return b;
    }

}
