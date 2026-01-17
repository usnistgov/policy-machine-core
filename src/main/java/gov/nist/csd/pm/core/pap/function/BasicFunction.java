package gov.nist.csd.pm.core.pap.function;

import gov.nist.csd.pm.core.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.core.pap.function.arg.type.Type;
import java.util.List;

public abstract non-sealed class BasicFunction<R> extends Function<Void, R>{

    public BasicFunction(String name,
                         Type<R> returnType,
                         List<FormalParameter<?>> parameters) {
        super(name, returnType, parameters);
    }
}
