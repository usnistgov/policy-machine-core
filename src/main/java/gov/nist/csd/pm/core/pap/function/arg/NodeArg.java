package gov.nist.csd.pm.core.pap.function.arg;

public sealed abstract class NodeArg<T> permits NameNodeArg, IdNodeArg{

    private T value;

    public NodeArg(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
