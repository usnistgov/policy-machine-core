package gov.nist.csd.pm.core.pap.pml.statement.operation;

import static gov.nist.csd.pm.core.pap.operation.Operation.NAME_PARAM;
import static gov.nist.csd.pm.core.pap.operation.prohibition.ProhibitionOp.ARSET_PARAM;
import static gov.nist.csd.pm.core.pap.operation.prohibition.ProhibitionOp.CONTAINERS_PARAM;
import static gov.nist.csd.pm.core.pap.operation.prohibition.ProhibitionOp.INTERSECTION_PARAM;
import static gov.nist.csd.pm.core.pap.operation.prohibition.ProhibitionOp.SUBJECT_PARAM;

import gov.nist.csd.pm.core.common.exception.PMException;
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

        return new Args()
            .put(NAME_PARAM, prohibition.getName())
            .put(SUBJECT_PARAM, prohibition.getSubject())
            .put(ARSET_PARAM, new ArrayList<>(prohibition.getAccessRightSet()))
            .put(INTERSECTION_PARAM, prohibition.isIntersection())
            .put(CONTAINERS_PARAM, new ArrayList<>(prohibition.getContainers()));
    }

    @Override
    public boolean exists(PAP pap, String name) throws PMException {
        return pap.query().prohibitions().prohibitionExists(name);
    }
}
