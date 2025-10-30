package gov.nist.csd.pm.core.pap.pml.statement.operation;

import static gov.nist.csd.pm.core.pap.function.op.Operation.NODE_PARAM;
import static gov.nist.csd.pm.core.pap.function.op.graph.GraphOp.DESCENDANTS_PARAM;
import static gov.nist.csd.pm.core.pap.function.op.graph.GraphOp.TYPE_PARAM;

import gov.nist.csd.pm.core.common.exception.NodeDoesNotExistException;
import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.node.NodeType;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.function.arg.Args;
import gov.nist.csd.pm.core.pap.function.op.graph.DeleteNodeOp;
import gov.nist.csd.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.core.pap.pml.expression.Expression;
import gov.nist.csd.pm.core.pap.query.GraphQuery;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import java.util.List;

public class DeleteNodeStatement extends DeleteStatement {

    public DeleteNodeStatement(Expression<String> expression, boolean ifExists) {
        super(new DeleteNodeOp(), Type.NODE, expression, ifExists);
    }

    @Override
    public Args prepareArgs(ExecutionContext ctx, PAP pap) throws PMException {
        // prepare for execution by replacing the name arg with the ID arg
        String name = nameExpression.execute(ctx, pap);

        try {
            GraphQuery graph = pap.query().graph();
            long nodeId = graph.getNodeId(name);
            NodeType nodeType = graph.getNodeById(nodeId).getType();
            LongArrayList descendants = new LongArrayList(graph.getAdjacentDescendants(nodeId));
            
            return new Args()
                .put(NODE_PARAM, nodeId)
                .put(TYPE_PARAM, nodeType.toString())
                .put(DESCENDANTS_PARAM, descendants);
        } catch (NodeDoesNotExistException e) {
            // if the node does not exist no error needs to occur, as the PAP will not error either
            return new Args()
                .put(NODE_PARAM, 0L)
                .put(TYPE_PARAM, NodeType.U.toString())
                .put(DESCENDANTS_PARAM, List.of());
        }
    }

    @Override
    public boolean exists(PAP pap, String name) throws PMException {
        return pap.query().graph().nodeExists(name);
    }
}