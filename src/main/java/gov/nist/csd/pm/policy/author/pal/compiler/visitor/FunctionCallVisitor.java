package gov.nist.csd.pm.policy.author.pal.compiler.visitor;

import gov.nist.csd.pm.policy.author.pal.antlr.PALBaseVisitor;
import gov.nist.csd.pm.policy.author.pal.antlr.PALParser;
import gov.nist.csd.pm.policy.author.pal.model.context.VisitorContext;
import gov.nist.csd.pm.policy.author.pal.model.expression.Type;
import gov.nist.csd.pm.policy.author.pal.model.scope.PALScopeException;
import gov.nist.csd.pm.policy.author.pal.model.scope.UnknownFunctionInScopeException;
import gov.nist.csd.pm.policy.author.pal.model.scope.UnknownVariableInScopeException;
import gov.nist.csd.pm.policy.author.pal.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.author.pal.statement.Expression;
import gov.nist.csd.pm.policy.author.pal.statement.FunctionStatement;
import gov.nist.csd.pm.policy.author.pal.model.function.FormalArgument;
import gov.nist.csd.pm.policy.exceptions.PMException;

import java.util.ArrayList;
import java.util.List;

public class FunctionCallVisitor extends PALBaseVisitor<FunctionStatement> {

    private final VisitorContext visitorCtx;

    public FunctionCallVisitor(VisitorContext visitorCtx) {
        this.visitorCtx = visitorCtx;
    }

    @Override
    public FunctionStatement visitFuncCall(PALParser.FuncCallContext ctx) {
        return parse(ctx);
    }

    @Override
    public FunctionStatement visitFuncCallStmt(PALParser.FuncCallStmtContext ctx) {
        return parse(ctx.funcCall());
    }

    private FunctionStatement parse(PALParser.FuncCallContext funcCallCtx) {
        String funcName = funcCallCtx.VARIABLE_OR_FUNCTION_NAME().getText();

        // get actual arg expressions
        PALParser.FuncCallArgsContext funcCallArgsCtx = funcCallCtx.funcCallArgs();
        List<Expression> actualArgs = new ArrayList<>();
        for (PALParser.ExpressionContext exprCtx : funcCallArgsCtx.expression()) {
            Expression expr = Expression.compile(visitorCtx, exprCtx);

            actualArgs.add(expr);
        }

        // check the function is in scope and the args are correct
        FunctionDefinitionStatement functionDefinitionStmt = null;
        try {
            functionDefinitionStmt = visitorCtx.scope().getFunction(funcName);
        } catch (UnknownFunctionInScopeException e) {
            visitorCtx.errorLog().addError(funcCallCtx, e.getMessage());
        }

        // if the stmt is null then there was a compilation error, just return the statement
        if (functionDefinitionStmt == null) {
            return new FunctionStatement(funcName, actualArgs);
        }

        // check that the actual args are correct type
        List<FormalArgument> formalArgs = functionDefinitionStmt.getArgs();

        if (formalArgs.size() != actualArgs.size()) {
            visitorCtx.errorLog().addError(
                    funcCallCtx,
                    "wrong number of args for function call " + funcName + ": " +
                            "expected " + formalArgs.size() + ", got " + actualArgs.size()
            );
        } else {
            for (int i = 0; i < actualArgs.size(); i++) {
                try {
                    Expression actual = actualArgs.get(i);
                    Type actualType = actual.getType(visitorCtx.scope());
                    FormalArgument formal = formalArgs.get(i);

                    if (!actual.getType(visitorCtx.scope()).equals(formal.type())) {
                        visitorCtx.errorLog().addError(
                                funcCallCtx,
                                "invalid argument type: expected " + formal.type() + ", got " +
                                        actualType + " at arg " + i
                        );
                    }
                } catch (PALScopeException e) {
                    visitorCtx.errorLog().addError(funcCallCtx, e.getMessage());
                }
            }
        }

        return new FunctionStatement(funcName, actualArgs);
    }
}
