package gov.nist.csd.pm.pap.pml.statement.operation;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.prohibition.Prohibition;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.executable.arg.ActualArgs;
import gov.nist.csd.pm.pap.executable.op.prohibition.ContainerConditionsList;
import gov.nist.csd.pm.pap.executable.op.prohibition.DeleteProhibitionOp;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.expression.Expression;

public class DeleteProhibitionStatement extends DeleteStatement<DeleteProhibitionOp> {

    public DeleteProhibitionStatement(Expression expression) {
        super(new DeleteProhibitionOp(), Type.PROHIBITION, expression);
    }

    @Override
    public ActualArgs prepareOperands(ExecutionContext ctx, PAP pap) throws PMException {
        String name = expression.execute(ctx, pap).getStringValue();

        Prohibition prohibition = pap.query().prohibitions().getProhibition(name);

        return op.actualArgs(prohibition.getName(),
            prohibition.getSubject(),
            prohibition.getAccessRightSet(),
            prohibition.isIntersection(),
            new ContainerConditionsList(prohibition.getContainers()));
    }
}
