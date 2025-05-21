package gov.nist.csd.pm.pap.pml.statement.operation;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.function.op.graph.AssociateOp;
import gov.nist.csd.pm.pap.function.op.graph.AssociateOp.AssociateOpArgs;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.expression.Expression;
import gov.nist.csd.pm.pap.query.GraphQuery;

import java.util.List;
import java.util.Objects;

public class AssociateStatement extends OperationStatement<AssociateOpArgs> {

    private final Expression<String> ua;
    private final Expression<String> target;
    private final Expression<List<String>> accessRights;

    public AssociateStatement(Expression<String> ua, Expression<String> target, Expression<List<String>> accessRights) {
        super(new AssociateOp());

        this.ua = ua;
        this.target = target;
        this.accessRights = accessRights;
    }

    @Override
    public AssociateOpArgs prepareArgs(ExecutionContext ctx, PAP pap) throws PMException {
        String uaName = ua.execute(ctx, pap);
        String targetName = target.execute(ctx, pap);
        AccessRightSet accessRightSet = new AccessRightSet(accessRights.execute(ctx, pap));

        GraphQuery graph = pap.query().graph();

        long uaId = graph.getNodeByName(uaName).getId();
        long targetId = graph.getNodeByName(targetName).getId();

        return new AssociateOpArgs(uaId, targetId, accessRightSet);
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