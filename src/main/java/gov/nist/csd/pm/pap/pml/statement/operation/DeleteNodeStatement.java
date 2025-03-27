package gov.nist.csd.pm.pap.pml.statement.operation;

import gov.nist.csd.pm.common.exception.NodeDoesNotExistException;
import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.graph.node.NodeType;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.executable.arg.ActualArgs;
import gov.nist.csd.pm.pap.executable.op.graph.DeleteNodeOp;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.expression.Expression;
import gov.nist.csd.pm.pap.query.GraphQuery;
import it.unimi.dsi.fastutil.longs.LongArrayList;

public class DeleteNodeStatement extends DeleteStatement<DeleteNodeOp> {

    public DeleteNodeStatement(Expression expression) {
        super(new DeleteNodeOp(), Type.NODE, expression);
    }

    @Override
    public ActualArgs prepareOperands(ExecutionContext ctx, PAP pap) throws PMException {
        // prepare for execution by replacing the name operand with the ID operand
        String name = expression.execute(ctx, pap).getStringValue();

        try {
            GraphQuery graph = pap.query().graph();
            long nodeId = graph.getNodeId(name);
            NodeType nodeType = graph.getNodeById(nodeId).getType();
            LongArrayList descendants = new LongArrayList(graph.getAdjacentDescendants(nodeId));
            
            return op.actualArgs(nodeId, nodeType, descendants);
        } catch (NodeDoesNotExistException e) {
            // if the node does not exist no error needs to occur, as the PAP will not error either
            return op.actualArgs(0L, NodeType.U, new LongArrayList());
        }
    }
} 