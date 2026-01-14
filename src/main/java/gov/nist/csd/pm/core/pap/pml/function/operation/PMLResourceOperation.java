package gov.nist.csd.pm.core.pap.pml.function.operation;

import static gov.nist.csd.pm.core.pap.function.arg.type.BasicTypes.VOID_TYPE;

import gov.nist.csd.pm.core.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.core.pap.function.op.ResourceOperation;
import gov.nist.csd.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.core.pap.pml.function.PMLFunction;
import gov.nist.csd.pm.core.pap.pml.statement.PMLStatementBlock;
import gov.nist.csd.pm.core.pap.pml.statement.PMLStatementSerializable;
import java.util.ArrayList;
import java.util.List;

public class PMLResourceOperation extends ResourceOperation implements PMLFunction, PMLStatementSerializable {

    private final List<FormalParameter<?>> pmlFormalParameters;
    private final PMLOperationSignature signature;
    private final PMLStatementBlock checks;
    private ExecutionContext ctx;

    public PMLResourceOperation(String name, List<FormalParameter<?>> formalParameters, PMLStatementBlock checks) {
        super(name, new ArrayList<>(formalParameters));

        this.pmlFormalParameters = formalParameters;
        this.signature = new PMLOperationSignature(name, VOID_TYPE, formalParameters, false);
        this.checks = checks;
    }

    public PMLResourceOperation(String name) {
        super(name, new ArrayList<>());

        this.pmlFormalParameters = new ArrayList<>();
        this.signature = new PMLOperationSignature(name, VOID_TYPE, new ArrayList<>(), false);
        this.checks = new PMLStatementBlock();
    }

    public List<FormalParameter<?>> getPmlFormalArgs() {
        return pmlFormalParameters;
    }

    public PMLOperationSignature getSignature() {
        return signature;
    }

    public PMLStatementBlock getChecks() {
        return checks;
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

    @Override
    public String toFormattedString(int indentLevel) {
        return String.format(
            "%s%s",
            getSignature().toFormattedString(indentLevel),
            checks.getStmts().isEmpty() ? "" : checks.toFormattedString(indentLevel)
        );
    }
}