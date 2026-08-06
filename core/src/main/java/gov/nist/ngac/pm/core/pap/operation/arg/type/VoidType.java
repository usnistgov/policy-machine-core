package gov.nist.ngac.pm.core.pap.operation.arg.type;

/**
 * Supported type for Void.
 */
public final class VoidType extends Type<Void> {

    @Override
    public Void cast(Object obj) {
        return null;
    }

}
