package gov.nist.csd.pm.core.pap.operation;

import gov.nist.csd.pm.core.pap.operation.arg.type.Type;
import gov.nist.csd.pm.core.pap.operation.param.FormalParameter;
import java.util.List;

public abstract non-sealed class AdminOperation<R> extends Operation<R> {

    public AdminOperation(String name,
                          Type<R> returnType,
                          List<FormalParameter<?>> parameters) {
        super(name, returnType, parameters);
    }
}
