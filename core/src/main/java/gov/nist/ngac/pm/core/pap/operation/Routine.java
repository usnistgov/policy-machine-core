package gov.nist.ngac.pm.core.pap.operation;

import gov.nist.ngac.pm.core.pap.operation.arg.type.Type;
import gov.nist.ngac.pm.core.pap.operation.param.FormalParameter;
import java.util.List;

public abstract non-sealed class Routine<R> extends Operation<R> {

    public Routine(String name,
                   Type<R> returnType,
                   List<FormalParameter<?>> parameters) {
        super(name, returnType, parameters, List.of());
    }
}
