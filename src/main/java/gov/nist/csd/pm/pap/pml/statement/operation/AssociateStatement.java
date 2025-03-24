package gov.nist.csd.pm.pap.pml.statement.operation;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.pap.op.graph.AssociateOp;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.expression.Expression;
import gov.nist.csd.pm.pap.pml.value.Value;
import gov.nist.csd.pm.pap.query.GraphQuery;

import java.util.Map;
import java.util.Objects;

import static gov.nist.csd.pm.pap.op.graph.AssociateOp.TARGET_OPERAND;
import static gov.nist.csd.pm.pap.op.graph.AssociateOp.UA_OPERAND;
import static gov.nist.csd.pm.pap.op.prohibition.ProhibitionOp.ARSET_OPERAND;


public class AssociateStatement extends OperationStatement {

    private final Expression ua;
    private final Expression target;
    private final Expression accessRights;

    public AssociateStatement(Expression ua, Expression target, Expression accessRights) {
        super(new AssociateOp());

        this.ua = ua;
        this.target = target;
        this.accessRights = accessRights;
    }

    @Override
    public Map<String, Object> prepareOperands(ExecutionContext ctx, PAP pap) throws PMException {
        Value uaValue = ua.execute(ctx, pap);
        Value targetValue = target.execute(ctx, pap);
        Value accessRightsValue = accessRights.execute(ctx, pap);

        AccessRightSet accessRightSet = new AccessRightSet();
        for (Value v : accessRightsValue.getArrayValue()) {
            accessRightSet.add(v.getStringValue());
        }

        GraphQuery graph = pap.query().graph();

        long uaId = graph.getNodeByName(uaValue.getStringValue()).getId();
        long targetId = graph.getNodeByName(targetValue.getStringValue()).getId();

        return Map.of(UA_OPERAND, uaId, TARGET_OPERAND, targetId, ARSET_OPERAND, accessRightSet);
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return indent(indentLevel) + String.format("associate %s and %s with %s",
                ua, target, accessRights);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AssociateStatement that)) return false;
        return Objects.equals(ua, that.ua) && Objects.equals(target, that.target) && Objects.equals(accessRights, that.accessRights);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ua, target, accessRights);
    }
}
