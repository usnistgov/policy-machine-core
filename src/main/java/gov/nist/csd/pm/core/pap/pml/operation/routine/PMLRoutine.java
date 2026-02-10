package gov.nist.csd.pm.core.pap.pml.operation.routine;

import gov.nist.csd.pm.core.pap.operation.Routine;
import gov.nist.csd.pm.core.pap.operation.arg.type.Type;
import gov.nist.csd.pm.core.pap.operation.param.FormalParameter;
import gov.nist.csd.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.core.pap.pml.operation.PMLOperation;
import gov.nist.csd.pm.core.pap.pml.operation.PMLOperationSignature;
import gov.nist.csd.pm.core.pap.pml.operation.PMLOperationSignature.OperationType;
import java.util.ArrayList;
import java.util.List;

public abstract class PMLRoutine<T> extends Routine<T> implements PMLOperation {

    private final Type<T> returnType;
    private final List<FormalParameter<?>> pmlFormalParameters;
    private final PMLOperationSignature signature;

    protected ExecutionContext ctx;

    public PMLRoutine(String name, Type<T> returnType, List<FormalParameter<?>> formalParameters) {
        super(name, returnType, new ArrayList<>(formalParameters));
        this.returnType = returnType;
        this.pmlFormalParameters = formalParameters;
        this.signature = new PMLOperationSignature(
            OperationType.ROUTINE,
            getName(),
            returnType,
            formalParameters,
            List.of());
    }

    public PMLOperationSignature getSignature() {
        return signature;
    }

    public Type<T> getReturnType() {
        return returnType;
    }

    public List<FormalParameter<?>> getPmlFormalArgs() {
        return pmlFormalParameters;
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
