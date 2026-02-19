package gov.nist.csd.pm.core.pap.pml.statement.operation;

import gov.nist.csd.pm.core.common.exception.NodeDoesNotExistException;
import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.operation.graph.DeleteNodeOp;
import gov.nist.csd.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.core.pap.pml.expression.Expression;
import gov.nist.csd.pm.core.pap.query.GraphQuery;

public class DeleteNodeStatement extends DeleteStatement {

    public DeleteNodeStatement(Expression<String> expression, boolean ifExists) {
        super(new DeleteNodeOp(), Type.NODE, expression, ifExists);
    }

    @Override
    public Args prepareArgs(ExecutionContext ctx, PAP pap) throws PMException {
        // prepare for execution by replacing the name arg with the ID arg
        String name = nameExpression.execute(ctx, pap);
        GraphQuery graph = pap.query().graph();

        long nodeId;
        try {
            nodeId = graph.getNodeId(name);
        } catch (NodeDoesNotExistException e) {
            // if the node does not exist no error needs to occur, as the PAP will not error either
            nodeId = 0;
        }

        return new Args()
            .put(DeleteNodeOp.DELETE_NODE_NODE_ID_PARAM, nodeId);
    }

    @Override
    public boolean exists(PAP pap, String name) throws PMException {
        return pap.query().graph().nodeExists(name);
    }
}