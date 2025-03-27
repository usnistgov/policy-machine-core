package gov.nist.csd.pm.pap.pml.executable.routine;

import gov.nist.csd.pm.pap.executable.routine.Routine;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.executable.arg.PMLFormalArg;
import gov.nist.csd.pm.pap.pml.type.Type;
import gov.nist.csd.pm.pap.pml.value.Value;

import java.util.ArrayList;
import java.util.List;

public abstract class PMLRoutine extends Routine<Value> {

    private final Type returnType;
    private final List<PMLFormalArg> pmlFormalArgs;
    private final PMLRoutineSignature signature;

    protected ExecutionContext ctx;

    public PMLRoutine(String name, Type returnType, List<PMLFormalArg> formalArgs) {
        super(name, new ArrayList<>(formalArgs));
        this.returnType = returnType;
        this.pmlFormalArgs = formalArgs;
        this.signature = new PMLRoutineSignature(
                getName(),
                returnType,
                formalArgs
        );
    }

    public PMLRoutineSignature getSignature() {
        return signature;
    }

    public Type getReturnType() {
        return returnType;
    }

    public List<PMLFormalArg> getPmlFormalArgs() {
        return pmlFormalArgs;
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
