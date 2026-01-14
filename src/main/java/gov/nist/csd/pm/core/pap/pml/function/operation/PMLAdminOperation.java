package gov.nist.csd.pm.core.pap.pml.function.operation;

import gov.nist.csd.pm.core.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.core.pap.function.arg.type.Type;
import gov.nist.csd.pm.core.pap.function.op.AdminOperation;
import gov.nist.csd.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.core.pap.pml.function.PMLFunction;

import java.util.ArrayList;
import java.util.List;

public abstract class PMLAdminOperation extends AdminOperation<Object> implements PMLFunction {

    private final Type<?> returnType;
    private final List<FormalParameter<?>> pmlFormalParameters;
    private final PMLOperationSignature signature;
    private ExecutionContext ctx;

    public PMLAdminOperation(String name, Type<?> returnType, List<FormalParameter<?>> formalParameters) {
        super(name, new ArrayList<>(formalParameters));

        this.returnType = returnType;
        this.pmlFormalParameters = formalParameters;
        this.signature = new PMLOperationSignature(name, returnType, formalParameters, true);
    }

    public PMLAdminOperation(String name, Type<?> returnType) {
        super(name, new ArrayList<>());

        this.returnType = returnType;
        this.pmlFormalParameters = new ArrayList<>();
        this.signature = new PMLOperationSignature(name, returnType, new ArrayList<>(), true);
    }

    public List<FormalParameter<?>> getPmlFormalArgs() {
        return pmlFormalParameters;
    }

    public PMLOperationSignature getSignature() {
        return signature;
    }

    public Type<?> getReturnType() {
        return returnType;
    }

    public ExecutionContext getCtx() {
        if (ctx == null) {
            throw new IllegalArgumentException("execution context has not been set");
        }

        return ctx;
    }

    public void setCtx(ExecutionContext ctx) {
        this.ctx = ctx;
    }

}
