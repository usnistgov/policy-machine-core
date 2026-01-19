package gov.nist.csd.pm.core.pap.pml.function.query;

import gov.nist.csd.pm.core.pap.function.QueryOperation;
import gov.nist.csd.pm.core.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.core.pap.function.arg.type.Type;
import gov.nist.csd.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.core.pap.pml.function.PMLFunction;
import java.util.List;

public abstract class PMLQueryOperation<T> extends QueryOperation<T> implements PMLFunction {

    private ExecutionContext ctx;

    public PMLQueryOperation(String name,
                             Type<T> returnType,
                             List<FormalParameter<?>> parameters) {
        super(name, returnType, parameters);
    }

    @Override
    public void setCtx(ExecutionContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public ExecutionContext getCtx() {
        return ctx;
    }
}
