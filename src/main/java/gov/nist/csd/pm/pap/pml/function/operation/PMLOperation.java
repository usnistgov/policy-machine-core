package gov.nist.csd.pm.pap.pml.function.operation;

import static gov.nist.csd.pm.pap.function.arg.type.ArgType.STRING_TYPE;

import gov.nist.csd.pm.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.pap.function.arg.MapArgs;
import gov.nist.csd.pm.pap.function.arg.type.ArgType;
import gov.nist.csd.pm.pap.function.op.Operation;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.function.PMLFunction;

import java.util.ArrayList;
import java.util.List;

public abstract class PMLOperation extends Operation<Object, MapArgs> implements PMLFunction {

    public static final FormalParameter<String> NODE_NAME_ARG = new FormalParameter<>("nodeName", STRING_TYPE);

    private final ArgType<?> returnType;
    private final List<FormalParameter<?>> pmlFormalParameters;
    private final PMLOperationSignature signature;
    protected ExecutionContext ctx;

    public PMLOperation(String name, ArgType<?> returnType, List<FormalParameter<?>> formalParameters) {
        super(name, new ArrayList<>(formalParameters));

        this.returnType = returnType;
        this.pmlFormalParameters = formalParameters;
        this.signature = new PMLOperationSignature(name, returnType, formalParameters);
    }

    public PMLOperation(String name, ArgType<?> returnType) {
        super(name, new ArrayList<>());

        this.returnType = returnType;
        this.pmlFormalParameters = new ArrayList<>();
        this.signature = new PMLOperationSignature(name, returnType, new ArrayList<>());
    }

    public List<FormalParameter<?>> getPmlFormalArgs() {
        return pmlFormalParameters;
    }

    public PMLOperationSignature getSignature() {
        return signature;
    }

    public ArgType<?> getReturnType() {
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
