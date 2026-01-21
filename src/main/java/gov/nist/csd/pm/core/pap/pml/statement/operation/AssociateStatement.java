package gov.nist.csd.pm.core.pap.pml.statement.operation;

import static gov.nist.csd.pm.core.pap.operation.Operation.ARSET_PARAM;
import static gov.nist.csd.pm.core.pap.operation.graph.AssociateOp.ASSOCIATE_TARGET_PARAM;
import static gov.nist.csd.pm.core.pap.operation.graph.AssociateOp.ASSOCIATE_UA_PARAM;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.operation.graph.AssociateOp;
import gov.nist.csd.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.core.pap.pml.expression.Expression;
import gov.nist.csd.pm.core.pap.query.GraphQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AssociateStatement extends OperationStatement {

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
    public Args prepareArgs(ExecutionContext ctx, PAP pap) throws PMException {
        String uaName = ua.execute(ctx, pap);
        String targetName = target.execute(ctx, pap);
        AccessRightSet accessRightSet = new AccessRightSet(accessRights.execute(ctx, pap));

        GraphQuery graph = pap.query().graph();

        long uaId = graph.getNodeByName(uaName).getId();
        long targetId = graph.getNodeByName(targetName).getId();

        return new Args()
            .put(ASSOCIATE_UA_PARAM, uaId)
            .put(ASSOCIATE_TARGET_PARAM, targetId)
            .put(ARSET_PARAM, new ArrayList<>(accessRightSet));
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