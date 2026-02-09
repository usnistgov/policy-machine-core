package gov.nist.csd.pm.core.pap.pml.operation.admin;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.operation.AdminOperation;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.operation.arg.type.Type;
import gov.nist.csd.pm.core.pap.operation.param.FormalParameter;
import gov.nist.csd.pm.core.pap.operation.reqcap.RequiredCapability;
import gov.nist.csd.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.core.pap.pml.operation.PMLOperation;
import gov.nist.csd.pm.core.pap.pml.operation.PMLOperationSignature;
import gov.nist.csd.pm.core.pap.pml.operation.PMLOperationSignature.OperationType;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import java.util.ArrayList;
import java.util.List;

public abstract class PMLAdminOperation<T> extends AdminOperation<T> implements PMLOperation {

    private final Type<T> returnType;
    private final List<FormalParameter<?>> pmlFormalParameters;
    private final PMLOperationSignature signature;
    private ExecutionContext ctx;

    public PMLAdminOperation(String name, Type<T> returnType, List<FormalParameter<?>> formalParameters, List<RequiredCapability> reqCaps) {
        super(name, returnType, new ArrayList<>(formalParameters), reqCaps);

        this.returnType = returnType;
        this.pmlFormalParameters = formalParameters;
        this.signature = new PMLOperationSignature(OperationType.ADMINOP, name, returnType, formalParameters, reqCaps);
    }

    public PMLAdminOperation(String name, Type<T> returnType, List<RequiredCapability> reqCaps) {
        super(name, returnType, new ArrayList<>(), reqCaps);

        this.returnType = returnType;
        this.pmlFormalParameters = new ArrayList<>();
        this.signature = new PMLOperationSignature(OperationType.ADMINOP, name, returnType, new ArrayList<>(), reqCaps);
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
