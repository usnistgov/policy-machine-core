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

import static gov.nist.ngac.pm.core.pap.operation.Operation.ARSET_PARAM;
import static gov.nist.ngac.pm.core.pap.operation.graph.AssociateOp.ASSOCIATE_TARGET_PARAM;
import static gov.nist.ngac.pm.core.pap.operation.graph.AssociateOp.ASSOCIATE_UA_PARAM;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.PAP;
import gov.nist.ngac.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.ngac.pm.core.pap.operation.accessright.AccessRightValidator;
import gov.nist.ngac.pm.core.pap.operation.arg.Args;
import gov.nist.ngac.pm.core.pap.operation.graph.AssociateOp;
import gov.nist.ngac.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.ngac.pm.core.pap.pml.expression.Expression;
import gov.nist.ngac.pm.core.pap.query.GraphQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A PML statement that associates a user attribute with a target using a set of access rights.
 */
public class AssociateStatement extends OperationStatement {

    private final Expression<String> ua;
    private final Expression<String> target;
    private final Expression<List<String>> accessRights;

    public AssociateStatement(Expression<String> ua, Expression<String> target, Expression<List<String>> accessRights) {
        super(new AssociateOp());

        this.ua = ua;
        this.target = target;
        this.accessRights = accessRights;
    }

    @Override
    public Args prepareArgs(ExecutionContext ctx, PAP pap) throws PMException {
        String uaName = ua.execute(ctx, pap);
        String targetName = target.execute(ctx, pap);
        AccessRightSet accessRightSet = new AccessRightSet(accessRights.execute(ctx, pap));

        // validate ars
        AccessRightValidator.validateAccessRights(pap.query().operations().getResourceAccessRights(), accessRightSet);

        GraphQuery graph = pap.query().graph();

        long uaId = graph.getNodeByName(uaName).getId();
        long targetId = graph.getNodeByName(targetName).getId();

        return new Args()
            .put(ASSOCIATE_UA_PARAM, uaId)
            .put(ASSOCIATE_TARGET_PARAM, targetId)
            .put(ARSET_PARAM, new ArrayList<>(accessRightSet));
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return indent(indentLevel) + String.format("associate %s to %s with %s",
                ua, target, accessRights);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AssociateStatement that)) return false;
        return Objects.equals(ua, that.ua) && Objects.equals(target, that.target) && Objects.equals(accessRights, that.accessRights);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ua, target, accessRights);
    }
} 