package gov.nist.csd.pm.core.pap.pml.statement.operation;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.prohibition.Prohibition;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.function.op.prohibition.DeleteProhibitionOp;
import gov.nist.csd.pm.core.pap.function.op.prohibition.ProhibitionOp.ProhibitionOpArgs;
import gov.nist.csd.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.core.pap.pml.expression.Expression;
import java.util.ArrayList;

public class DeleteProhibitionStatement extends DeleteStatement<ProhibitionOpArgs> {

    public DeleteProhibitionStatement(Expression<String> expression, boolean ifExists) {
        super(new DeleteProhibitionOp(), Type.PROHIBITION, expression, ifExists);
    }

    @Override
    public ProhibitionOpArgs prepareArgs(ExecutionContext ctx, PAP pap) throws PMException {
        String name = nameExpression.execute(ctx, pap);

        Prohibition prohibition = pap.query().prohibitions().getProhibition(name);

        return new ProhibitionOpArgs(prohibition.getName(),
            prohibition.getSubject(),
            prohibition.getAccessRightSet(),
            prohibition.isIntersection(),
            new ArrayList<>(prohibition.getContainers()));
    }

    @Override
    public boolean exists(PAP pap, String name) throws PMException {
        return pap.query().prohibitions().prohibitionExists(name);
    }
}
