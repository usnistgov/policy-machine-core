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

package gov.nist.ngac.pm.core.pap.pml.compiler.visitor;

import static gov.nist.ngac.pm.core.common.graph.node.NodeType.OA;
import static gov.nist.ngac.pm.core.pap.operation.arg.type.BasicTypes.STRING_TYPE;

import gov.nist.ngac.pm.core.common.graph.node.NodeType;
import gov.nist.ngac.pm.core.pap.operation.arg.type.ListType;
import gov.nist.ngac.pm.core.pap.pml.antlr.PMLParser;
import gov.nist.ngac.pm.core.pap.pml.context.VisitorContext;
import gov.nist.ngac.pm.core.pap.pml.expression.Expression;
import gov.nist.ngac.pm.core.pap.pml.statement.operation.CreateNonPCStatement;
import java.util.List;


/**
 * Compiles a PML create OA/UA/O/U ... in ... statement into a {@link CreateNonPCStatement}.
 */
public class CreateNonPCStmtVisitor extends PMLBaseVisitor<CreateNonPCStatement> {

    public CreateNonPCStmtVisitor(VisitorContext visitorCtx) {
        super(visitorCtx);
    }

    @Override
    public CreateNonPCStatement visitCreateNonPCStatement(PMLParser.CreateNonPCStatementContext ctx) {
        NodeType type = getNodeType(ctx.nonPCNodeType());
        Expression<String> name = ExpressionVisitor.compile(visitorCtx, ctx.name, STRING_TYPE);
        Expression<List<String>> assignTo = ExpressionVisitor.compile(visitorCtx, ctx.in, ListType.of(STRING_TYPE));

        return new CreateNonPCStatement(name, type, assignTo);
    }

    private NodeType getNodeType(PMLParser.NonPCNodeTypeContext nodeType) {
        if (nodeType.OA() != null) {
            return OA;
        } else if (nodeType.UA() != null) {
            return NodeType.UA;
        } else if (nodeType.O() != null) {
            return NodeType.O;
        } else {
            return NodeType.U;
        }
    }
}
