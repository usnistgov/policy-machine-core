package gov.nist.csd.pm.core.pap.pml.statement.operation;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.node.Properties;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.function.op.graph.SetNodePropertiesOp;
import gov.nist.csd.pm.core.pap.function.op.graph.SetNodePropertiesOp.SetNodePropertiesOpArgs;
import gov.nist.csd.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.core.pap.pml.expression.Expression;


import java.util.Map;
import java.util.Objects;

public class SetNodePropertiesStatement extends OperationStatement<SetNodePropertiesOpArgs> {

    private final Expression<String> nameExpr;
    private final Expression<Map<String, String>> propertiesExpr;

    public SetNodePropertiesStatement(Expression<String> nameExpr, Expression<Map<String, String>> propertiesExpr) {
        super(new SetNodePropertiesOp());
        this.nameExpr = nameExpr;
        this.propertiesExpr = propertiesExpr;
    }

    @Override
    public SetNodePropertiesOpArgs prepareArgs(ExecutionContext ctx, PAP pap) throws PMException {
        String name = nameExpr.execute(ctx, pap);
        Map<String, String> map = propertiesExpr.execute(ctx, pap);

        long id = pap.query().graph().getNodeId(name);
        Properties properties = new Properties(map);
        
        return new SetNodePropertiesOpArgs(id, properties);
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return indent(indentLevel) + String.format("set properties of %s to %s", nameExpr, propertiesExpr);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SetNodePropertiesStatement that)) return false;
        return Objects.equals(nameExpr, that.nameExpr) && Objects.equals(propertiesExpr, that.propertiesExpr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nameExpr, propertiesExpr);
    }
} 