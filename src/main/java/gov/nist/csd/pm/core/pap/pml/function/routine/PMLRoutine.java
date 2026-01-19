package gov.nist.csd.pm.core.pap.pml.function.routine;

import gov.nist.csd.pm.core.pap.function.Routine;
import gov.nist.csd.pm.core.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.core.pap.function.arg.type.Type;
import gov.nist.csd.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.core.pap.pml.function.PMLFunction;
import java.util.ArrayList;
import java.util.List;

public abstract class PMLRoutine<T> extends Routine<T> implements PMLFunction {

    private final Type<T> returnType;
    private final List<FormalParameter<?>> pmlFormalParameters;
    private final PMLRoutineSignature signature;

    protected ExecutionContext ctx;

    public PMLRoutine(String name, Type<T> returnType, List<FormalParameter<?>> formalParameters) {
        super(name, returnType, new ArrayList<>(formalParameters));
        this.returnType = returnType;
        this.pmlFormalParameters = formalParameters;
        this.signature = new PMLRoutineSignature(
                getName(),
                returnType,
            formalParameters
        );
    }

    public PMLRoutineSignature getSignature() {
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
