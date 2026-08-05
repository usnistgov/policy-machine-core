package gov.nist.ngac.pm.core.pap.operation.arg.type;

/**
 * The PML type of an operation with no return value. Casting always yields null, regardless of input.
 */
public final class VoidType extends Type<Void> {

    @Override
    public Void cast(Object obj) {
        return null;
    }

}
