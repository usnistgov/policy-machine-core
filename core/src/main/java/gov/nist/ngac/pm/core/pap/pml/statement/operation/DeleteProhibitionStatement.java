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

import static gov.nist.ngac.pm.core.pap.operation.Operation.NAME_PARAM;
import static gov.nist.ngac.pm.core.pap.operation.prohibition.ProhibitionOp.EXCLUSION_SET_PARAM;
import static gov.nist.ngac.pm.core.pap.operation.prohibition.ProhibitionOp.INCLUSION_SET_PARAM;
import static gov.nist.ngac.pm.core.pap.operation.prohibition.ProhibitionOp.NODE_ID_PARAM;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.common.prohibition.NodeProhibition;
import gov.nist.ngac.pm.core.common.prohibition.ProcessProhibition;
import gov.nist.ngac.pm.core.common.prohibition.Prohibition;
import gov.nist.ngac.pm.core.pap.PAP;
import gov.nist.ngac.pm.core.pap.operation.arg.Args;
import gov.nist.ngac.pm.core.pap.operation.prohibition.DeleteProhibitionOp;
import gov.nist.ngac.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.ngac.pm.core.pap.pml.expression.Expression;
import java.util.ArrayList;

/**
 * A PML statement that deletes a prohibition.
 */
public class DeleteProhibitionStatement extends DeleteStatement {

    public DeleteProhibitionStatement(Expression<String> expression, boolean ifExists) {
        super(new DeleteProhibitionOp(), Type.PROHIBITION, expression, ifExists);
    }

    @Override
    public Args prepareArgs(ExecutionContext ctx, PAP pap) throws PMException {
        String name = nameExpression.execute(ctx, pap);

        Prohibition prohibition = pap.query().prohibitions().getProhibition(name);

        long nodeId = switch (prohibition) {
            case NodeProhibition nodeProhibition -> nodeProhibition.getNodeId();
            case ProcessProhibition processProhibition -> processProhibition.getUserId();
        };

        return new Args()
            .put(NAME_PARAM, prohibition.getName())
            .put(NODE_ID_PARAM, nodeId)
            .put(INCLUSION_SET_PARAM, new ArrayList<>(prohibition.getInclusionSet()))
            .put(EXCLUSION_SET_PARAM, new ArrayList<>(prohibition.getExclusionSet()));
    }

    @Override
    public boolean exists(PAP pap, String name) throws PMException {
        return pap.query().prohibitions().prohibitionExists(name);
    }
}
