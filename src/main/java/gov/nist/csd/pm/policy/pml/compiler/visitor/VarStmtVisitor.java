package gov.nist.csd.pm.policy.pml.compiler.visitor;

import gov.nist.csd.pm.policy.pml.antlr.PMLParser;
import gov.nist.csd.pm.policy.pml.compiler.Variable;
import gov.nist.csd.pm.policy.pml.expression.Expression;
import gov.nist.csd.pm.policy.pml.expression.literal.LiteralVisitor;
import gov.nist.csd.pm.policy.pml.context.VisitorContext;
import gov.nist.csd.pm.policy.pml.scope.PMLScopeException;
import gov.nist.csd.pm.policy.pml.statement.*;
import gov.nist.csd.pm.policy.pml.type.Type;

import java.util.ArrayList;
import java.util.List;

public class VarStmtVisitor extends PMLBaseVisitor<PMLStatement> {


    public VarStmtVisitor(VisitorContext visitorCtx) {
        super(visitorCtx);
    }

    @Override
    public VariableDeclarationStatement visitConstDeclaration(PMLParser.ConstDeclarationContext ctx) {
        List<VariableDeclarationStatement.Declaration> decls = new ArrayList<>();
        for (PMLParser.ConstSpecContext constSpecContext : ctx.constSpec()) {
            String varName = constSpecContext.ID().getText();
            Expression expr = compileConstLiteral(constSpecContext.literal());

            try {
                if (visitorCtx.scope().variableExists(varName) && visitorCtx.scope().getVariable(varName).isConst()) {
                    visitorCtx.errorLog().addError(ctx, "const '" + varName + "' already defined in scope");

                    return new VariableDeclarationStatement(ctx);
                }

                visitorCtx.scope().addVariable(varName, new Variable(varName, expr.getType(visitorCtx.scope()), true));
            } catch (PMLScopeException e) {
                visitorCtx.errorLog().addError(ctx, e.getMessage());

                return new VariableDeclarationStatement(ctx);
            }

            decls.add(new VariableDeclarationStatement.Declaration(varName, expr));
        }

        return new VariableDeclarationStatement(true, decls);
    }

    private Expression compileConstLiteral(PMLParser.LiteralContext literalContext) {
        LiteralVisitor literalVisitor = new LiteralVisitor(visitorCtx);
        if (literalContext instanceof PMLParser.StringLiteralContext stringLiteralContext) {
            return literalVisitor.visitStringLiteral(stringLiteralContext);
        } else if (literalContext instanceof PMLParser.BoolLiteralContext boolLiteralContext) {
            return literalVisitor.visitBoolLiteral(boolLiteralContext);
        } else if (literalContext instanceof PMLParser.ArrayLiteralContext arrayLiteralContext) {
            return literalVisitor.visitArrayLiteral(arrayLiteralContext);
        } else {
            PMLParser.MapLiteralContext mapLiteralContext = (PMLParser.MapLiteralContext) literalContext;
            return literalVisitor.visitMapLiteral(mapLiteralContext);
        }
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
                visitorCtx.errorLog().addError(ctx, e.getMessage());

                return new VariableDeclarationStatement(ctx);
            }

            decls.add(new VariableDeclarationStatement.Declaration(varName, expr));
        }

        return new VariableDeclarationStatement(false, decls);
    }

    @Override
    public PMLStatement visitShortDeclaration(PMLParser.ShortDeclarationContext ctx) {
        String varName = ctx.ID().getText();
        Expression expr = Expression.compile(visitorCtx, ctx.expression(), Type.any());

        ShortDeclarationStatement stmt = new ShortDeclarationStatement(varName, expr);

        try {
            if (visitorCtx.scope().variableExists(varName)) {
                visitorCtx.errorLog().addError(ctx, "variable " + varName + " already exists");

                return new ShortDeclarationStatement(ctx);
            }

            visitorCtx.scope().addVariable(varName, new Variable(varName, expr.getType(visitorCtx.scope()), false));
        } catch (PMLScopeException e) {
            visitorCtx.errorLog().addError(ctx, e.getMessage());

            return new ShortDeclarationStatement(ctx);
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
                visitorCtx.errorLog().addError(ctx, "cannot reassign const variable");

                return new VariableAssignmentStatement(ctx);
            }

            // don't need to update variable since the name and type are the only thing that matter during compilation
        } catch (PMLScopeException e) {
            visitorCtx.errorLog().addError(ctx, e.getMessage());

            return new VariableAssignmentStatement(ctx);
        }

        return stmt;
    }
}
