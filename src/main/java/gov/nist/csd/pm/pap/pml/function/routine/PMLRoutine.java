package gov.nist.csd.pm.pap.pml.function.routine;

import gov.nist.csd.pm.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.pap.function.arg.Args;
import gov.nist.csd.pm.pap.function.arg.type.ArgType;
import gov.nist.csd.pm.pap.function.routine.Routine;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.function.PMLFunction;

import java.util.ArrayList;
import java.util.List;

public abstract class PMLRoutine extends Routine<Object, Args> implements PMLFunction {

    private final ArgType<?> returnType;
    private final List<FormalParameter<?>> pmlFormalParameters;
    private final PMLRoutineSignature signature;

    protected ExecutionContext ctx;

    public PMLRoutine(String name, ArgType<?> returnType, List<FormalParameter<?>> formalParameters) {
        super(name, new ArrayList<>(formalParameters));
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

    public ArgType<?> getReturnType() {
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
