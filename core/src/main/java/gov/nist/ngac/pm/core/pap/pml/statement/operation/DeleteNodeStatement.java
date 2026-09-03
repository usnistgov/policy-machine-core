/*
 * This Software (Policy Machine) is being made available as a public service by the
 * National Institute of Standards and Technology (NIST), an Agency of the United
 * States Department of Commerce. This software was developed in part by employees of
 * NIST and in part by NIST contractors. Copyright in portions of this software that
 * were developed by NIST contractors has been licensed or assigned to NIST. Pursuant
 * to Title 17 United States Code Section 105, works of NIST employees are not
 * subject to copyright protection in the United States. However, NIST may hold
 * international copyright in software created by its employees and domestic
 * copyright (or licensing rights) in portions of software that were assigned or
 * licensed to NIST. To the extent that NIST holds copyright in this software, it is
 * being made available under the Creative Commons Attribution 4.0 International
 * license (CC BY 4.0). The disclaimers of the CC BY 4.0 license apply to all parts
 * of the software developed or licensed by NIST.
 *
 * ACCESS THE FULL CC BY 4.0 LICENSE HERE:
 * https://creativecommons.org/licenses/by/4.0/legalcode
 */

package gov.nist.ngac.pm.core.pap.pml.statement.operation;

import gov.nist.ngac.pm.core.common.exception.NodeDoesNotExistException;
import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.PAP;
import gov.nist.ngac.pm.core.pap.operation.arg.Args;
import gov.nist.ngac.pm.core.pap.operation.graph.DeleteNodeOp;
import gov.nist.ngac.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.ngac.pm.core.pap.pml.expression.Expression;
import gov.nist.ngac.pm.core.pap.query.GraphQuery;

/**
 * A PML statement that deletes a node. If the node does not exist, {@link #prepareArgs} resolves to
 * id 0 rather than failing, matching the PAP's no-op behavior for deleting a nonexistent node.
 */
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