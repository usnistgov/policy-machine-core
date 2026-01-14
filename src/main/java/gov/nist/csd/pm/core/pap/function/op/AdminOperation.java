package gov.nist.csd.pm.core.pap.function.op;

import gov.nist.csd.pm.core.pap.function.arg.FormalParameter;
import java.util.List;

public abstract non-sealed class AdminOperation<R> extends Operation<R> {

    public AdminOperation(String name,
                          List<FormalParameter<?>> parameters) {
        super(name, parameters);
    }
}
