package gov.nist.csd.pm.pap.pml.statement.operation;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.op.Operation;
import gov.nist.csd.pm.common.op.graph.DeleteNodeOp;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.expression.Expression;

import java.util.Map;

import static gov.nist.csd.pm.common.op.Operation.ID_OPERAND;

public class DeleteNodeStatement extends DeleteStatement {

	public DeleteNodeStatement(Expression expression) {
		super(new DeleteNodeOp(), Type.NODE, expression);
	}

	@Override
	public Map<String, Object> prepareOperands(ExecutionContext ctx, PAP pap) throws PMException {
		// prepare for execution by replacing the name operand with the ID operand
		String name = getExpression().execute(ctx, pap).getStringValue();
		long nodeId = pap.query().graph().getNodeId(name);
		return Map.of(ID_OPERAND, nodeId);
	}
}
