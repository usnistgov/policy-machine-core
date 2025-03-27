package gov.nist.csd.pm.pap.pml.statement.operation;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.obligation.Obligation;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.executable.arg.ActualArgs;
import gov.nist.csd.pm.pap.executable.op.obligation.DeleteObligationOp;
import gov.nist.csd.pm.pap.executable.op.obligation.RuleList;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.expression.Expression;

public class DeleteObligationStatement extends DeleteStatement<DeleteObligationOp> {

    public DeleteObligationStatement(Expression expression) {
        super(new DeleteObligationOp(), Type.OBLIGATION, expression);
    }

    @Override
    public ActualArgs prepareOperands(ExecutionContext ctx, PAP pap) throws PMException {
        String name = expression.execute(ctx, pap).getStringValue();

        Obligation obligation = pap.query().obligations().getObligation(name);

        return op.actualArgs(obligation.getAuthorId(), obligation.getName(), new RuleList(obligation.getRules()));
    }
}
