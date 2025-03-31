package gov.nist.csd.pm.pap.pml.function.arg;

import gov.nist.csd.pm.pap.function.arg.FormalArg;
import gov.nist.csd.pm.pap.function.arg.type.ArgType;
import gov.nist.csd.pm.pap.pml.type.Type;

public class WrappedFormalArg<T> extends PMLFormalArg {

    private FormalArg<?> unwrapped;

    public WrappedFormalArg(FormalArg<T> t, Type type) {
        super(t.getName(), type);
        unwrapped = t;
    }

    public FormalArg<?> unwrap() {
        return unwrapped;
    }
}
