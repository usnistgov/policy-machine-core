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

import gov.nist.ngac.pm.core.pap.operation.arg.type.ListType;
import gov.nist.ngac.pm.core.pap.pml.antlr.PMLParser;
import gov.nist.ngac.pm.core.pap.pml.context.VisitorContext;
import gov.nist.ngac.pm.core.pap.pml.expression.Expression;
import gov.nist.ngac.pm.core.pap.pml.expression.literal.ArrayLiteralExpression;
import gov.nist.ngac.pm.core.pap.pml.statement.operation.CreateProhibitionStatement;
import java.util.ArrayList;
import java.util.List;

/**
 * Compiles a PML create ... prohibition ... statement into a {@link CreateProhibitionStatement},
 * choosing a node or process prohibition based on the parsed entity clause.
 */
public class CreateProhibitionStmtVisitor extends PMLBaseVisitor<CreateProhibitionStatement> {

    public CreateProhibitionStmtVisitor(VisitorContext visitorCtx) {
        super(visitorCtx);
    }

    @Override
    public CreateProhibitionStatement visitCreateProhibitionStatement(PMLParser.CreateProhibitionStatementContext ctx) {
        Expression<String> name = ExpressionVisitor.compile(visitorCtx, ctx.name, STRING_TYPE);
        Expression<String> node = ExpressionVisitor.compile(visitorCtx, ctx.node, STRING_TYPE);

        boolean isConjunctive = ctx.type.getType() == PMLParser.CONJ;
        boolean isProcessProhibition = ctx.entity.getType() == PMLParser.PROCESS;

        Expression<List<String>> arset = ExpressionVisitor.compile(visitorCtx, ctx.arset, ListType.of(STRING_TYPE));
        Expression<List<String>> inclusionSet = new ArrayLiteralExpression<>(new ArrayList<>(), STRING_TYPE);
        if (ctx.inclusionSet != null) {
            inclusionSet = ExpressionVisitor.compile(visitorCtx, ctx.inclusionSet, ListType.of(STRING_TYPE));
        }

        Expression<List<String>> exclusionSet = new ArrayLiteralExpression<>(new ArrayList<>(), STRING_TYPE);
        if (ctx.exclusionSet != null) {
            exclusionSet = ExpressionVisitor.compile(visitorCtx, ctx.exclusionSet, ListType.of(STRING_TYPE));
        }

        if (isProcessProhibition) {
            Expression<String> process = ExpressionVisitor.compile(visitorCtx, ctx.process, STRING_TYPE);
            return CreateProhibitionStatement.processProhibition(name, node, process, arset, inclusionSet, exclusionSet, isConjunctive);
        }

        return CreateProhibitionStatement.nodeProhibition(name, node, arset, inclusionSet, exclusionSet, isConjunctive);
    }
}
