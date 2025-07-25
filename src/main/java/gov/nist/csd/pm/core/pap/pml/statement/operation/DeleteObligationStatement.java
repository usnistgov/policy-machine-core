package gov.nist.csd.pm.core.pap.pml.statement.operation;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.function.op.obligation.ObligationOp.ObligationOpArgs;
import gov.nist.csd.pm.core.pap.obligation.Obligation;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.function.op.obligation.DeleteObligationOp;
import gov.nist.csd.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.core.pap.pml.expression.Expression;
import java.util.ArrayList;

public class DeleteObligationStatement extends DeleteStatement<ObligationOpArgs> {

    public DeleteObligationStatement(Expression<String> expression, boolean ifExists) {
        super(new DeleteObligationOp(), Type.OBLIGATION, expression, ifExists);
    }

    @Override
    public ObligationOpArgs prepareArgs(ExecutionContext ctx, PAP pap) throws PMException {
        String name = nameExpression.execute(ctx, pap);

        Obligation obligation = pap.query().obligations().getObligation(name);

        return new ObligationOpArgs(obligation.getAuthorId(), obligation.getName(), new ArrayList<>(obligation.getRules()));
    }

    @Override
    public boolean exists(PAP pap, String name) throws PMException {
        return pap.query().obligations().obligationExists(name);
    }
}
