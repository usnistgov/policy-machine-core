package gov.nist.csd.pm.core.pap.pml.statement.operation;

import static gov.nist.csd.pm.core.pap.operation.Operation.ARSET_PARAM;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.operation.accessrights.AccessRightSet;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.operation.operation.SetResourceAccessRights;
import gov.nist.csd.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.core.pap.pml.expression.Expression;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SetResourceAccessRightsStatement extends OperationStatement {

    private final Expression<List<String>> arsExpr;

    public SetResourceAccessRightsStatement(Expression<List<String>> arsExpr) {
        super(new SetResourceAccessRights());
        this.arsExpr = arsExpr;
    }

    @Override
    public Args prepareArgs(ExecutionContext ctx, PAP pap) throws PMException {
        List<String> opValues = arsExpr.execute(ctx, pap);
        AccessRightSet accessRightSet = new AccessRightSet(opValues);

        return new Args()
            .put(ARSET_PARAM, new ArrayList<>(accessRightSet));
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return indent(indentLevel) + "set resource access rights " + arsExpr;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SetResourceAccessRightsStatement that)) return false;
        return Objects.equals(arsExpr, that.arsExpr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(arsExpr);
    }
} 