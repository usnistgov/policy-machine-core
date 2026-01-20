package gov.nist.csd.pm.core.pap.pml.statement.operation;

import static gov.nist.csd.pm.core.pap.operation.Operation.NAME_PARAM;
import static gov.nist.csd.pm.core.pap.operation.obligation.ObligationOp.EVENT_PATTERN_PARAM;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.obligation.Obligation;
import gov.nist.csd.pm.core.pap.operation.obligation.DeleteObligationOp;
import gov.nist.csd.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.core.pap.pml.expression.Expression;

public class DeleteObligationStatement extends DeleteStatement {

    public DeleteObligationStatement(Expression<String> expression, boolean ifExists) {
        super(new DeleteObligationOp(), Type.OBLIGATION, expression, ifExists);
    }

    @Override
    public Args prepareArgs(ExecutionContext ctx, PAP pap) throws PMException {
        String name = nameExpression.execute(ctx, pap);

        Obligation obligation = pap.query().obligations().getObligation(name);

        return new Args()
            .put(NAME_PARAM, obligation.getName())
            .put(EVENT_PATTERN_PARAM, obligation.getEventPattern());
    }

    @Override
    public boolean exists(PAP pap, String name) throws PMException {
        return pap.query().obligations().obligationExists(name);
    }
}
