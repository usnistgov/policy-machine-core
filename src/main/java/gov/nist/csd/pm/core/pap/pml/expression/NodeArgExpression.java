package gov.nist.csd.pm.core.pap.pml.expression;

import static gov.nist.csd.pm.core.pap.function.arg.type.BasicTypes.NODE_TYPE;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.function.arg.NameNodeArg;
import gov.nist.csd.pm.core.pap.function.arg.NodeArg;
import gov.nist.csd.pm.core.pap.function.arg.type.Type;
import gov.nist.csd.pm.core.pap.pml.context.ExecutionContext;
import java.util.Objects;

public class NodeArgExpression extends Expression<NodeArg<?>> {

    private Expression<String> nameExpr;

    public NodeArgExpression(Expression<String> nameExpr) {
        this.nameExpr = nameExpr;
    }

    @Override
    public Type<NodeArg<?>> getType() {
        return NODE_TYPE;
    }

    @Override
    public NodeArg<?> execute(ExecutionContext ctx, PAP pap) throws PMException {
        return new NameNodeArg(nameExpr.execute(ctx, pap));
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return nameExpr.toFormattedString(indentLevel);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NodeArgExpression that)) {
            return false;
        }
        return Objects.equals(nameExpr, that.nameExpr);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(nameExpr);
    }
}