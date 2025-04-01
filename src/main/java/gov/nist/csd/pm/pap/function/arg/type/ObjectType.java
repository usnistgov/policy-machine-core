package gov.nist.csd.pm.pap.function.arg.type;

public final class ObjectType extends ArgType<Object> {

    @Override
    public Object cast(Object obj) {
        return obj;
    }
}
