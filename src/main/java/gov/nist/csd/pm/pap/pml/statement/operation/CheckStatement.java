package gov.nist.csd.pm.pap.pml.statement.operation;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.PrivilegeChecker;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.expression.Expression;
import gov.nist.csd.pm.pap.pml.statement.PMLStatement;
import gov.nist.csd.pm.pap.pml.value.Value;
import gov.nist.csd.pm.pap.pml.value.VoidValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CheckStatement implements PMLStatement {

    private Expression arsExpr;
    private Expression targetExpr;

    public CheckStatement(Expression arsExpr, Expression targetExpr) {
        this.arsExpr = arsExpr;
        this.targetExpr = targetExpr;
    }

    @Override
    public Value execute(ExecutionContext ctx, PAP pap) throws PMException {
        Value ars = arsExpr.execute(ctx, pap);
        Value target = targetExpr.execute(ctx, pap);

        List<String> arsToCheck = new ArrayList<>();
        if (ars.getType().isString()) {
            arsToCheck.add(ars.getStringValue());
        } else {
            List<Value> arrayValue = ars.getArrayValue();
            for (Value v : arrayValue) {
                arsToCheck.add(v.getStringValue());
            }
        }

        PrivilegeChecker privilegeChecker = new PrivilegeChecker(pap);
        privilegeChecker.setExplain(ctx.isExplain());

        if (target.getType().isString()) {
            long id = pap.query().graph().getNodeByName(target.getStringValue()).getId();
            privilegeChecker.check(ctx.author(), id, arsToCheck);
        } else {
            List<Value> arrayValue = target.getArrayValue();
            for (Value value : arrayValue) {
                long id = pap.query().graph().getNodeByName(value.getStringValue()).getId();
                privilegeChecker.check(ctx.author(), id, arsToCheck);
            }
        }

        return new VoidValue();
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return indent(indentLevel) +
                "check " + arsExpr.toFormattedString(0) +
                " on " + targetExpr.toFormattedString(0);
    }

    @Override
    public String toString() {
        return toFormattedString(0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CheckStatement that)) return false;
        return Objects.equals(arsExpr, that.arsExpr) && Objects.equals(targetExpr, that.targetExpr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(arsExpr, targetExpr);
    }
}
