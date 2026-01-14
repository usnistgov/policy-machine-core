package gov.nist.csd.pm.core.pap.pml.statement.operation;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.function.arg.Args;
import gov.nist.csd.pm.core.pap.function.op.Operation;
import gov.nist.csd.pm.core.pap.function.op.operation.DeleteAdminOperationOp;
import gov.nist.csd.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.core.pap.pml.expression.Expression;

public class DeleteAdminOpStatement extends DeleteStatement{

    public DeleteAdminOpStatement(Expression<String> nameExpression, boolean ifExists) {
        super(new DeleteAdminOperationOp(), Type.ADMIN_OP, nameExpression, ifExists);
    }

    @Override
    public boolean exists(PAP pap, String name) throws PMException {
        return pap.query().operations().getAdminOperationNames().contains(name);
    }

    @Override
    public Args prepareArgs(ExecutionContext ctx, PAP pap) throws PMException {
        String name = nameExpression.execute(ctx, pap);

        return new Args()
            .put(Operation.NAME_PARAM, name);
    }
}