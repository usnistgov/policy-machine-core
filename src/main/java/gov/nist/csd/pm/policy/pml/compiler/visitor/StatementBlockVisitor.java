package gov.nist.csd.pm.policy.pml.compiler.visitor;

import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.pml.antlr.PMLParser;
import gov.nist.csd.pm.policy.pml.context.VisitorContext;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.pml.statement.FunctionReturnStatement;
import gov.nist.csd.pm.policy.pml.statement.IfStatement;
import gov.nist.csd.pm.policy.pml.statement.PMLStatement;
import gov.nist.csd.pm.policy.pml.type.Type;

import java.util.ArrayList;
import java.util.List;

public class StatementBlockVisitor extends PMLBaseVisitor<StatementBlockVisitor.Result> {

    private Type returnType;

    public StatementBlockVisitor(VisitorContext visitorCtx, Type retyurnType) {
        super(visitorCtx);
        this.returnType = retyurnType;
    }

    @Override
    public Result visitStatementBlock(PMLParser.StatementBlockContext ctx) {
        List<PMLStatement> stmts = new ArrayList<>();
        StatementVisitor statementVisitor = new StatementVisitor(visitorCtx);
        for (PMLParser.StatementContext statementContext : ctx.statement()) {
            PMLStatement pmlStatement = statementVisitor.visitStatement(statementContext);

            if (pmlStatement instanceof FunctionDefinitionStatement) {
                visitorCtx.errorLog().addError(statementContext, "functions are not allowed inside statement blocks");
            }

            stmts.add(pmlStatement);
        }

        try {
            boolean allPathsReturned = allPathsReturned(stmts, returnType);
            return new Result(allPathsReturned, stmts);
        } catch (PMException e) {
            visitorCtx.errorLog().addError(ctx, e.getMessage());

            // returning anything here will be ignored because of the error
            return new Result(true, stmts);
        }
    }
    private boolean allPathsReturned(List<PMLStatement> statements, Type returnType)
            throws PMException {
        if (statements.isEmpty()) {
            return false;
        }

        PMLStatement lastStmt = statements.get(statements.size() - 1);
        if (lastStmt instanceof FunctionReturnStatement functionReturnStatement) {
            if (!functionReturnStatement.matchesReturnType(returnType, visitorCtx.scope())) {
                throw new PMException("return statement \"" + functionReturnStatement + "\" does not match return type " + returnType);
            }

            return true;
        }


        boolean allPathsReturned = false;
        PMLStatement pmlStatement;
        for (int i = 0; i < statements.size(); i++) {
            pmlStatement = statements.get(i);

            if (pmlStatement instanceof FunctionReturnStatement functionReturnStatement) {
                if (i < statements.size() - 1) {
                    throw new PMException("function return should be last statement in block");
                }

                if (!functionReturnStatement.matchesReturnType(returnType, visitorCtx.scope())) {
                    throw new PMException("return statement \"" + functionReturnStatement + "\" does not match return type " + returnType);
                }

                return true;
            } else if (pmlStatement instanceof IfStatement ifStatement) {
                if (!allIfStatementPathsReturned(ifStatement, returnType)) {
                    return false;
                } else {
                    allPathsReturned = true;
                }
            }
        }

        return allPathsReturned;
    }

    private boolean allIfStatementPathsReturned(IfStatement ifStatement, Type returnType)
            throws PMException {
        boolean check = allPathsReturned(ifStatement.getIfBlock().block(), returnType);
        if (!check) {
            return false;
        }

        // check else ifs
        for (IfStatement.ConditionalBlock conditionalBlock : ifStatement.getIfElseBlocks()) {
            check = allPathsReturned(conditionalBlock.block(), returnType);
            if (!check) {
                return false;
            }
        }

        // check else
        check = allPathsReturned(ifStatement.getElseBlock(), returnType);
        if (!check) {
            return false;
        }

        return true;
    }

    record Result(boolean allPathsReturned, List<PMLStatement> stmts) {

    }
}
