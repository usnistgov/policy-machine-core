package gov.nist.csd.pm.pap.pml.statement.operation;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.prohibition.Prohibition;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.function.arg.Args;
import gov.nist.csd.pm.pap.function.op.prohibition.DeleteProhibitionOp;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.expression.Expression;
import java.util.ArrayList;

public class DeleteProhibitionStatement extends DeleteStatement<DeleteProhibitionOp> {

    public DeleteProhibitionStatement(Expression expression) {
        super(new DeleteProhibitionOp(), Type.PROHIBITION, expression);
    }

    @Override
    public Args prepareArgs(ExecutionContext ctx, PAP pap) throws PMException {
        String name = expression.execute(ctx, pap).getStringValue();

        Prohibition prohibition = pap.query().prohibitions().getProhibition(name);

        return op.actualArgs(prohibition.getName(),
            prohibition.getSubject(),
            prohibition.getAccessRightSet(),
            prohibition.isIntersection(),
            new ArrayList<>(prohibition.getContainers()));
    }
}
