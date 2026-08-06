package gov.nist.ngac.pm.core.pap.pml.operation.resource;

import gov.nist.ngac.pm.core.pap.operation.ResourceOperation;
import gov.nist.ngac.pm.core.pap.operation.arg.type.Type;
import gov.nist.ngac.pm.core.pap.operation.param.FormalParameter;
import gov.nist.ngac.pm.core.pap.operation.reqcap.RequiredCapability;
import gov.nist.ngac.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.ngac.pm.core.pap.pml.operation.PMLOperation;
import gov.nist.ngac.pm.core.pap.pml.operation.PMLOperationSignature;
import gov.nist.ngac.pm.core.pap.pml.operation.PMLOperationSignature.OperationType;
import java.util.ArrayList;
import java.util.List;

/**
 * Base class for resource operations defined in PML, carrying the {@link PMLOperationSignature} and
 * {@link ExecutionContext} a {@link PMLOperation} needs.
 *
 * @param <T> the operation's return type
 */
public abstract class PMLResourceOperation<T> extends ResourceOperation<T> implements PMLOperation {

    private final Type<T> returnType;
    private final List<FormalParameter<?>> pmlFormalParameters;
    private final PMLOperationSignature signature;
    private ExecutionContext ctx;

    public PMLResourceOperation(String name, Type<T> returnType, List<FormalParameter<?>> formalParameters, List<RequiredCapability> reqCaps) {
        super(name, returnType, new ArrayList<>(formalParameters), reqCaps);

        this.returnType = returnType;
        this.pmlFormalParameters = formalParameters;
        this.signature = new PMLOperationSignature(OperationType.RESOURCEOP, name, returnType, formalParameters, reqCaps);
    }

    public PMLResourceOperation(String name, Type<T> returnType, List<FormalParameter<?>> formalParameters,
                                List<FormalParameter<?>> eventParameters, List<RequiredCapability> reqCaps) {
        super(name, returnType, new ArrayList<>(formalParameters), eventParameters, reqCaps);

        this.returnType = returnType;
        this.pmlFormalParameters = formalParameters;
        this.signature = new PMLOperationSignature(OperationType.RESOURCEOP, name, returnType, formalParameters, eventParameters, reqCaps);
    }

    public List<FormalParameter<?>> getPmlFormalArgs() {
        return pmlFormalParameters;
    }

    public PMLOperationSignature getSignature() {
        return signature;
    }

    public Type<T> getReturnType() {
        return returnType;
    }

    /**
     * Returns the execution context this operation is currently running under.
     *
     * @throws IllegalArgumentException if no context has been set yet
     */
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