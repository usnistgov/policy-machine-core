package gov.nist.csd.pm.core.pap.pml.statement.operation;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.function.arg.Args;
import gov.nist.csd.pm.core.pap.function.op.operation.SetResourceOperationsOp;
import gov.nist.csd.pm.core.pap.function.op.operation.SetResourceOperationsOp.SetResourceOperationsOpArgs;
import gov.nist.csd.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.core.pap.pml.expression.Expression;

import java.util.List;
import java.util.Objects;

public class SetResourceOperationsStatement extends OperationStatement<SetResourceOperationsOpArgs> {

    private final Expression<List<String>> operationsExpr;

    public SetResourceOperationsStatement(Expression<List<String>> operationsExpr) {
        super(new SetResourceOperationsOp());
        this.operationsExpr = operationsExpr;
    }

    @Override
    public SetResourceOperationsOpArgs prepareArgs(ExecutionContext ctx, PAP pap) throws PMException {
        List<String> opValues = operationsExpr.execute(ctx, pap);
        AccessRightSet accessRightSet = new AccessRightSet(opValues);

        return new SetResourceOperationsOpArgs(accessRightSet);
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return indent(indentLevel) + "set resource operations " + operationsExpr;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SetResourceOperationsStatement that)) return false;
        return Objects.equals(operationsExpr, that.operationsExpr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(operationsExpr);
    }
} 