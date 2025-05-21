package gov.nist.csd.pm.pap.function.arg.type;

public final class VoidType extends Type<Void> {

    @Override
    public Void cast(Object obj) {
        return null;
    }

}
