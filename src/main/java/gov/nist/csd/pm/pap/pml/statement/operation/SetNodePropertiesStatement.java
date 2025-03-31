package gov.nist.csd.pm.pap.pml.statement.operation;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.graph.node.Properties;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.function.arg.Args;
import gov.nist.csd.pm.pap.function.op.graph.SetNodePropertiesOp;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.expression.Expression;
import gov.nist.csd.pm.pap.pml.value.Value;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SetNodePropertiesStatement extends OperationStatement<SetNodePropertiesOp> {

    private final Expression nameExpr;
    private final Expression propertiesExpr;

    public SetNodePropertiesStatement(Expression nameExpr, Expression propertiesExpr) {
        super(new SetNodePropertiesOp());
        this.nameExpr = nameExpr;
        this.propertiesExpr = propertiesExpr;
    }

    @Override
    public Args prepareOperands(ExecutionContext ctx, PAP pap) throws PMException {
        String name = nameExpr.execute(ctx, pap).getStringValue();
        Map<Value, Value> map = propertiesExpr.execute(ctx, pap).getMapValue();
        Map<String, String> propertiesMap = new HashMap<>();
        for (Value key : map.keySet()) {
            propertiesMap.put(key.getStringValue(), map.get(key).getStringValue());
        }

        long id = pap.query().graph().getNodeId(name);
        Properties properties = new Properties(propertiesMap);
        
        return op.actualArgs(id, properties);
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