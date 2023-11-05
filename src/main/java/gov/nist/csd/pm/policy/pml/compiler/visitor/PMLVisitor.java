package gov.nist.csd.pm.policy.pml.compiler.visitor;

import gov.nist.csd.pm.policy.pml.antlr.PMLParser;
import gov.nist.csd.pm.policy.pml.antlr.PMLParserBaseVisitor;
import gov.nist.csd.pm.policy.pml.model.context.VisitorContext;
import gov.nist.csd.pm.policy.pml.statement.PMLStatement;

import java.util.ArrayList;
import java.util.List;

public class PMLVisitor extends PMLParserBaseVisitor<List<PMLStatement>> {

    private final VisitorContext visitorCtx;

    public PMLVisitor(VisitorContext visitorCtx) {
        this.visitorCtx = visitorCtx;
    }

    @Override
    public List<PMLStatement> visitPml(PMLParser.PmlContext ctx) {
        List<PMLStatement> statements = new ArrayList<>();

        List<PMLParser.ConstDeclarationContext> constantCtxs = new ArrayList<>();
        List<PMLParser.FunctionDefinitionStatementContext> functionCtxs = new ArrayList<>();
        List<PMLParser.StatementContext> statementCtxs = new ArrayList<>();

        for (PMLParser.StatementContext stmtCtx : ctx.statement()) {
            if (stmtCtx.variableDeclarationStatement() != null) {
                if (stmtCtx.variableDeclarationStatement() instanceof PMLParser.ConstDeclarationContext) {
                    constantCtxs.add((PMLParser.ConstDeclarationContext) stmtCtx.variableDeclarationStatement());
                } else {
                    statementCtxs.add(stmtCtx);
                }
            } else if (stmtCtx.functionDefinitionStatement() != null) {
                functionCtxs.add(stmtCtx.functionDefinitionStatement());
            } else {
                statementCtxs.add(stmtCtx);
            }
        }

        statements.addAll(compileTopLevelConstants(constantCtxs));
        statements.addAll(compileTopLevelFunctions(functionCtxs));
        statements.addAll(compileStatements(statementCtxs));

        return statements;
    }

    private List<PMLStatement> compileTopLevelFunctions(
            List<PMLParser.FunctionDefinitionStatementContext> functionSignatureCtxs) {
        List<PMLStatement> statements = new ArrayList<>();

        FunctionDefinitionVisitor.FunctionSignatureVisitor functionSignatureVisitor =
                new FunctionDefinitionVisitor.FunctionSignatureVisitor(visitorCtx);
        FunctionDefinitionVisitor functionDefinitionVisitor = new FunctionDefinitionVisitor(visitorCtx);


        // compile top level function signatures
        for (PMLParser.FunctionDefinitionStatementContext functionDefinitionStatementContext : functionSignatureCtxs) {
            // visit the signature which will add to the scope
            functionSignatureVisitor.visitFunctionSignature(functionDefinitionStatementContext.functionSignature());
        }

        // compile function bodies
        for (PMLParser.FunctionDefinitionStatementContext functionDefinitionStatementContext : functionSignatureCtxs) {
            // visit the definition which will return the statement with body
            PMLStatement funcStmt = functionDefinitionVisitor.visitFunctionDefinitionStatement(functionDefinitionStatementContext);

            statements.add(funcStmt);
        }


        return statements;
    }

    private List<PMLStatement> compileTopLevelConstants(List<PMLParser.ConstDeclarationContext> constantCtxs) {
        List<PMLStatement> statements = new ArrayList<>();

        for (PMLParser.ConstDeclarationContext constantCtx : constantCtxs) {
            PMLStatement constStmt = new VarStmtVisitor(visitorCtx)
                    .visitConstDeclaration(constantCtx);

            statements.add(constStmt);
        }

        return statements;
    }

    private List<PMLStatement> compileStatements(List<PMLParser.StatementContext> statementCtxs) {
        List<PMLStatement> statements = new ArrayList<>();
        for (PMLParser.StatementContext stmtCtx : statementCtxs) {
            StatementVisitor statementVisitor = new StatementVisitor(visitorCtx);
            PMLStatement statement = statementVisitor.visitStatement(stmtCtx);
            statements.add(statement);
        }

        return statements;
    }

}
