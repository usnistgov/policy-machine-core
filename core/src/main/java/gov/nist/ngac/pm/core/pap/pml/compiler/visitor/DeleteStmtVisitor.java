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

import static gov.nist.ngac.pm.core.pap.operation.arg.type.BasicTypes.STRING_TYPE;

import gov.nist.ngac.pm.core.pap.pml.antlr.PMLParser.DeleteNodeContext;
import gov.nist.ngac.pm.core.pap.pml.antlr.PMLParser.DeleteObligationContext;
import gov.nist.ngac.pm.core.pap.pml.antlr.PMLParser.DeleteOperationContext;
import gov.nist.ngac.pm.core.pap.pml.antlr.PMLParser.DeleteProhibitionContext;
import gov.nist.ngac.pm.core.pap.pml.antlr.PMLParser.DeleteStatementContext;
import gov.nist.ngac.pm.core.pap.pml.antlr.PMLParser.DeleteTypeContext;
import gov.nist.ngac.pm.core.pap.pml.context.VisitorContext;
import gov.nist.ngac.pm.core.pap.pml.expression.Expression;
import gov.nist.ngac.pm.core.pap.pml.statement.operation.DeleteNodeStatement;
import gov.nist.ngac.pm.core.pap.pml.statement.operation.DeleteObligationStatement;
import gov.nist.ngac.pm.core.pap.pml.statement.operation.DeleteOperationStatement;
import gov.nist.ngac.pm.core.pap.pml.statement.operation.DeleteProhibitionStatement;
import gov.nist.ngac.pm.core.pap.pml.statement.operation.DeleteStatement;



/**
 * Compiles a PML delete statement into the matching {@link DeleteStatement} subclass.
 */
public class DeleteStmtVisitor extends PMLBaseVisitor<DeleteStatement> {

    public DeleteStmtVisitor(VisitorContext visitorCtx) {
        super(visitorCtx);
    }

    @Override
    public DeleteStatement visitDeleteStatement(DeleteStatementContext ctx) {
        Expression<String> nameExpr = ExpressionVisitor.compile(visitorCtx, ctx.expression(), STRING_TYPE);
        boolean ifExists = ctx.IF_EXISTS() != null;

        DeleteTypeContext deleteTypeCtx = ctx.deleteType();
        return switch (deleteTypeCtx) {
            case DeleteNodeContext deleteNodeContext -> new DeleteNodeStatement(nameExpr, ifExists);
            case DeleteProhibitionContext deleteProhibitionContext -> new DeleteProhibitionStatement(nameExpr, ifExists);
            case DeleteObligationContext deleteObligationContext -> new DeleteObligationStatement(nameExpr, ifExists);
            case DeleteOperationContext deleteAdminOpContext -> new DeleteOperationStatement(nameExpr, ifExists);
            default -> throw new IllegalStateException("Unexpected value: " + deleteTypeCtx);
        };
    }
}
