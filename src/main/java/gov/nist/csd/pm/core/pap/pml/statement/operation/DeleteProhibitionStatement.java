package gov.nist.csd.pm.core.pap.pml.statement.operation;

import static gov.nist.csd.pm.core.pap.operation.Operation.NAME_PARAM;
import static gov.nist.csd.pm.core.pap.operation.prohibition.ProhibitionOp.ARSET_PARAM;
import static gov.nist.csd.pm.core.pap.operation.prohibition.ProhibitionOp.EXCLUSION_SET_PARAM;
import static gov.nist.csd.pm.core.pap.operation.prohibition.ProhibitionOp.INCLUSION_SET_PARAM;
import static gov.nist.csd.pm.core.pap.operation.prohibition.ProhibitionOp.NODE_ID_PARAM;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.prohibition.NodeProhibition;
import gov.nist.csd.pm.core.common.prohibition.ProcessProhibition;
import gov.nist.csd.pm.core.common.prohibition.Prohibition;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.operation.prohibition.DeleteProhibitionOp;
import gov.nist.csd.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.core.pap.pml.expression.Expression;
import java.util.ArrayList;

public class DeleteProhibitionStatement extends DeleteStatement {

    public DeleteProhibitionStatement(Expression<String> expression, boolean ifExists) {
        super(new DeleteProhibitionOp(), Type.PROHIBITION, expression, ifExists);
    }

    @Override
    public Args prepareArgs(ExecutionContext ctx, PAP pap) throws PMException {
        String name = nameExpression.execute(ctx, pap);

        Prohibition prohibition = pap.query().prohibitions().getProhibition(name);

        long nodeId = switch (prohibition) {
            case NodeProhibition nodeProhibition -> nodeProhibition.getNodeId();
            case ProcessProhibition processProhibition -> processProhibition.getUserId();
        };

        return new Args()
            .put(NAME_PARAM, prohibition.getName())
            .put(NODE_ID_PARAM, nodeId)
            .put(INCLUSION_SET_PARAM, new ArrayList<>(prohibition.getInclusionSet()))
            .put(EXCLUSION_SET_PARAM, new ArrayList<>(prohibition.getExclusionSet()));
    }

    @Override
    public boolean exists(PAP pap, String name) throws PMException {
        return pap.query().prohibitions().prohibitionExists(name);
    }
}
