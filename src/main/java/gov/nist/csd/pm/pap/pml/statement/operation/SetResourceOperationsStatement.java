package gov.nist.csd.pm.pap.pml.statement.operation;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.function.arg.ActualArgs;
import gov.nist.csd.pm.pap.function.op.operation.SetResourceOperationsOp;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.expression.Expression;
import gov.nist.csd.pm.pap.pml.value.Value;

import java.util.List;
import java.util.Objects;

public class SetResourceOperationsStatement extends OperationStatement<SetResourceOperationsOp> {

    private final Expression operationsExpr;

    public SetResourceOperationsStatement(Expression operationsExpr) {
        super(new SetResourceOperationsOp());
        this.operationsExpr = operationsExpr;
    }

    @Override
    public ActualArgs prepareOperands(ExecutionContext ctx, PAP pap) throws PMException {
        List<Value> opValues = operationsExpr.execute(ctx, pap).getArrayValue();
        AccessRightSet accessRightSet = new AccessRightSet();
        for (Value opValue : opValues) {
            accessRightSet.add(opValue.getStringValue());
        }
        
        return op.actualArgs(accessRightSet);
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