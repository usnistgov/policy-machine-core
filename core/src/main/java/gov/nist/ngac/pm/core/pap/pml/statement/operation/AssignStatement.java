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

import static gov.nist.ngac.pm.core.pap.operation.graph.AssignOp.ASSIGN_ASCENDANT_PARAM;
import static gov.nist.ngac.pm.core.pap.operation.graph.AssignOp.ASSIGN_DESCENDANTS_PARAM;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.PAP;
import gov.nist.ngac.pm.core.pap.operation.arg.Args;
import gov.nist.ngac.pm.core.pap.operation.graph.AssignOp;
import gov.nist.ngac.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.ngac.pm.core.pap.pml.expression.Expression;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A PML statement that assigns a node to one or more descendant nodes.
 */
public class AssignStatement extends OperationStatement {

    private final Expression<String> ascendant;
    private final Expression<List<String>> descendants;

    public AssignStatement(Expression<String> ascendant, Expression<List<String>> descendants) {
        super(new AssignOp());

        this.ascendant = ascendant;
        this.descendants = descendants;
    }

    @Override
    public Args prepareArgs(ExecutionContext ctx, PAP pap) throws PMException {
        String asc = ascendant.execute(ctx, pap);
        List<String> descs = descendants.execute(ctx, pap);

        // convert to ids
        long ascId = pap.query().graph().getNodeByName(asc).getId();
        List<Long> descIds = new ArrayList<>();
        for (String desc : descs) {
            descIds.add(pap.query().graph().getNodeByName(desc).getId());
        }

        return new Args()
            .put(ASSIGN_ASCENDANT_PARAM, ascId)
            .put(ASSIGN_DESCENDANTS_PARAM, descIds);
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return indent(indentLevel) + String.format("assign %s to %s", ascendant, descendants);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AssignStatement that)) return false;
        return Objects.equals(ascendant, that.ascendant) && Objects.equals(descendants, that.descendants);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ascendant, descendants);
    }
} 