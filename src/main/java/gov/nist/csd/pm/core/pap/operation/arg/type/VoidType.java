package gov.nist.csd.pm.core.pap.operation.arg.type;

public final class VoidType extends Type<Void> {

    @Override
    public Void cast(Object obj) {
        return null;
    }

}
