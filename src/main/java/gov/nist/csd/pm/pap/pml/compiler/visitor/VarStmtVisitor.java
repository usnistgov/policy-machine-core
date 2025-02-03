package gov.nist.csd.pm.pap.pml.compiler.visitor;

import gov.nist.csd.pm.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.pap.pml.compiler.Variable;
import gov.nist.csd.pm.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.pap.pml.exception.PMLCompilationRuntimeException;
import gov.nist.csd.pm.pap.pml.expression.Expression;
import gov.nist.csd.pm.pap.pml.scope.PMLScopeException;
import gov.nist.csd.pm.pap.pml.statement.*;
import gov.nist.csd.pm.pap.pml.type.Type;

import java.util.ArrayList;
import java.util.List;

public class VarStmtVisitor extends PMLBaseVisitor<PMLStatementSerializable> {


    public VarStmtVisitor(VisitorContext visitorCtx) {
        super(visitorCtx);
    }

    @Override
    public PMLStatement visitVarDeclaration(PMLParser.VarDeclarationContext ctx) {
        List<VariableDeclarationStatement.Declaration> decls = new ArrayList<>();
        for (PMLParser.VarSpecContext varSpecContext : ctx.varSpec()) {
            String varName = varSpecContext.ID().getText();
            Expression expr = Expression.compile(visitorCtx, varSpecContext.expression(), Type.any());

            try {
                visitorCtx.scope().addVariable(varName, new Variable(varName, expr.getType(visitorCtx.scope()), false));
            } catch (PMLScopeException e) {
                throw new PMLCompilationRuntimeException(ctx, e.getMessage());
            }

            decls.add(new VariableDeclarationStatement.Declaration(varName, expr));
        }

        return new VariableDeclarationStatement(decls);
    }

    @Override
    public PMLStatement visitShortDeclaration(PMLParser.ShortDeclarationContext ctx) {
        String varName = ctx.ID().getText();
        Expression expr = Expression.compile(visitorCtx, ctx.expression(), Type.any());

        ShortDeclarationStatement stmt = new ShortDeclarationStatement(varName, expr);

        try {
            if (visitorCtx.scope().variableExists(varName)) {
                throw new PMLCompilationRuntimeException(ctx, "variable " + varName + " already exists");
            }

            visitorCtx.scope().addVariable(varName, new Variable(varName, expr.getType(visitorCtx.scope()), false));
        } catch (PMLScopeException e) {
            throw new PMLCompilationRuntimeException(ctx, e.getMessage());
        }

        return stmt;
    }

    @Override
    public PMLStatement visitVariableAssignmentStatement(PMLParser.VariableAssignmentStatementContext ctx) {
        String varName = ctx.ID().getText();
        Expression expr = Expression.compile(visitorCtx, ctx.expression(), Type.any());

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
