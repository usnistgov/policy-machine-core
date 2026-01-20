package gov.nist.csd.pm.core.pap.pml.statement.operation;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.operation.Operation;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.operation.operation.DeleteOperationOp;
import gov.nist.csd.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.core.pap.pml.expression.Expression;

public class DeleteOperationStatement extends DeleteStatement{

    public DeleteOperationStatement(Expression<String> nameExpression, boolean ifExists) {
        super(new DeleteOperationOp(), Type.ADMIN_OP, nameExpression, ifExists);
    }

    @Override
    public boolean exists(PAP pap, String name) throws PMException {
        return pap.query().operations().operationExists(name);
    }

    @Override
    public Args prepareArgs(ExecutionContext ctx, PAP pap) throws PMException {
        String name = nameExpression.execute(ctx, pap);

        return new Args()
            .put(Operation.NAME_PARAM, name);
    }
}