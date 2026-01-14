package gov.nist.csd.pm.core.pap.pml.compiler.visitor;

import static gov.nist.csd.pm.core.pap.function.arg.type.BasicTypes.ANY_TYPE;

import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.core.pap.pml.compiler.Variable;
import gov.nist.csd.pm.core.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.core.pap.pml.exception.PMLCompilationRuntimeException;
import gov.nist.csd.pm.core.pap.pml.expression.Expression;
import gov.nist.csd.pm.core.pap.pml.scope.PMLScopeException;
import gov.nist.csd.pm.core.pap.pml.statement.PMLStatement;
import gov.nist.csd.pm.core.pap.pml.statement.PMLStatementSerializable;
import gov.nist.csd.pm.core.pap.pml.statement.basic.ShortDeclarationStatement;
import gov.nist.csd.pm.core.pap.pml.statement.basic.VariableAssignmentStatement;
import gov.nist.csd.pm.core.pap.pml.statement.basic.VariableDeclarationStatement;
import java.util.ArrayList;
import java.util.List;

public class VarStmtVisitor extends PMLBaseVisitor<PMLStatementSerializable> {


    public VarStmtVisitor(VisitorContext visitorCtx) {
        super(visitorCtx);
    }

    @Override
    public PMLStatement<?> visitVarDeclaration(PMLParser.VarDeclarationContext ctx) {
        List<VariableDeclarationStatement.Declaration> decls = new ArrayList<>();
        for (PMLParser.VarSpecContext varSpecContext : ctx.varSpec()) {
            String varName = varSpecContext.ID().getText();
            Expression<Object> expr = ExpressionVisitor.compile(visitorCtx, varSpecContext.expression(), ANY_TYPE);

            try {
                visitorCtx.scope().addVariable(varName, new Variable(varName, expr.getType(), false));
            } catch (PMLScopeException e) {
                throw new PMLCompilationRuntimeException(ctx, e.getMessage());
            }

            decls.add(new VariableDeclarationStatement.Declaration(varName, expr));
        }

        return new VariableDeclarationStatement(decls);
    }

    @Override
    public PMLStatement<?> visitShortDeclaration(PMLParser.ShortDeclarationContext ctx) {
        String varName = ctx.ID().getText();
        Expression<Object> expr = ExpressionVisitor.compile(visitorCtx, ctx.expression(), ANY_TYPE);

        ShortDeclarationStatement stmt = new ShortDeclarationStatement(varName, expr);

        try {
            if (visitorCtx.scope().variableExists(varName)) {
                throw new PMLCompilationRuntimeException(ctx, "variable " + varName + " already exists");
            }

            visitorCtx.scope().addVariable(varName, new Variable(varName, expr.getType(), false));
        } catch (PMLScopeException e) {
            throw new PMLCompilationRuntimeException(ctx, e.getMessage());
        }

        return stmt;
    }

    @Override
    public PMLStatement<?> visitVariableAssignmentStatement(PMLParser.VariableAssignmentStatementContext ctx) {
        String varName = ctx.ID().getText();
        Expression<Object> expr = ExpressionVisitor.compile(visitorCtx, ctx.expression(), ANY_TYPE);

        VariableAssignmentStatement stmt = new VariableAssignmentStatement(
                varName,
                ctx.PLUS() != null,
                expr
        );

        try {
           if (visitorCtx.scope().getVariable(varName).isConst()) {
                throw new PMLCompilationRuntimeException(ctx, "cannot reassign const variable");
            }

            // don't need to update variable since the name and type are the only thing that matter during compilation
        } catch (PMLScopeException e) {
            throw new PMLCompilationRuntimeException(ctx, e.getMessage());
        }

        return stmt;
    }
}
