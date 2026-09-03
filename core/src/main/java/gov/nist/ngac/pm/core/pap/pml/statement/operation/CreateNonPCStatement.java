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

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.common.graph.node.NodeType;
import gov.nist.ngac.pm.core.pap.PAP;
import gov.nist.ngac.pm.core.pap.operation.Operation;
import gov.nist.ngac.pm.core.pap.operation.arg.Args;
import gov.nist.ngac.pm.core.pap.operation.graph.CreateNodeOp;
import gov.nist.ngac.pm.core.pap.operation.graph.CreateObjectAttributeOp;
import gov.nist.ngac.pm.core.pap.operation.graph.CreateObjectOp;
import gov.nist.ngac.pm.core.pap.operation.graph.CreateUserAttributeOp;
import gov.nist.ngac.pm.core.pap.operation.graph.CreateUserOp;
import gov.nist.ngac.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.ngac.pm.core.pap.pml.expression.Expression;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A PML statement that creates a node of any type other than a policy class.
 */
public class CreateNonPCStatement extends OperationStatement {

    private final NodeType nodeType;
    private final Expression<String> nameExpr;
    private final Expression<List<String>> inExpr;

    public CreateNonPCStatement(Expression<String> nameExpr, NodeType nodeType, Expression<List<String>> inExpr) {
        super(getOpFromType(nodeType));
        this.nodeType = nodeType;
        this.nameExpr = nameExpr;
        this.inExpr = inExpr;
    }

    @Override
    public Args prepareArgs(ExecutionContext ctx, PAP pap) throws PMException {
        String name = nameExpr.execute(ctx, pap);
        List<String> inList = inExpr.execute(ctx, pap);

        List<Long> descIds = new ArrayList<>();
        for (String parentName : inList) {
            descIds.add(pap.query().graph().getNodeByName(parentName).getId());
        }

        return new Args()
            .put(NAME_PARAM, name)
            .put(CreateNodeOp.CREATE_NODE_DESCENDANTS_PARAM, descIds);
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return indent(indentLevel) + String.format("create %s %s in %s", nodeType.toString(), nameExpr, inExpr);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CreateNonPCStatement that)) return false;
        return nodeType == that.nodeType && 
               Objects.equals(nameExpr, that.nameExpr) && 
               Objects.equals(inExpr, that.inExpr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nodeType, nameExpr, inExpr);
    }

    private static Operation<Long> getOpFromType(NodeType type) {
        return switch (type) {
            case OA -> new CreateObjectAttributeOp();
            case O -> new CreateObjectOp();
            case UA -> new CreateUserAttributeOp();
            default -> new CreateUserOp();
        };
    }
} 