package gov.nist.csd.pm.policy.author.pal.compiler.visitor;

import gov.nist.csd.pm.policy.author.pal.antlr.PALBaseVisitor;
import gov.nist.csd.pm.policy.author.pal.antlr.PALParser;
import gov.nist.csd.pm.policy.author.pal.model.context.VisitorContext;
import gov.nist.csd.pm.policy.author.pal.model.expression.Type;
import gov.nist.csd.pm.policy.author.pal.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.author.pal.statement.Expression;
import gov.nist.csd.pm.policy.author.pal.statement.FunctionStatement;
import gov.nist.csd.pm.policy.author.pal.model.function.FormalArgument;

import java.util.ArrayList;
import java.util.List;

public class FunctionCallVisitor extends PALBaseVisitor<FunctionStatement> {

    private final VisitorContext visitorCtx;

    public FunctionCallVisitor(VisitorContext visitorCtx) {
        this.visitorCtx = visitorCtx;
    }

    @Override
    public FunctionStatement visitFunctionCall(PALParser.FunctionCallContext ctx) {
        PALParser.FuncCallContext funcCallCtx = ctx.funcCall();
        return parse(funcCallCtx);
    }

    @Override
    public FunctionStatement visitFuncCallStmt(PALParser.FuncCallStmtContext ctx) {
        return parse(ctx.funcCall());
    }

    private FunctionStatement parse(PALParser.FuncCallContext funcCallCtx) {
        String funcName = funcCallCtx.IDENTIFIER().getText();

        // get actual arg expressions
        PALParser.FuncCallArgsContext funcCallArgsCtx = funcCallCtx.funcCallArgs();
        List<Expression> actualArgs = new ArrayList<>();
        for (PALParser.ExpressionContext exprCtx : funcCallArgsCtx.expression()) {
            Expression expr = Expression.compile(visitorCtx, exprCtx);

            actualArgs.add(expr);
        }

        // check the function is in scope and the args are correct
        FunctionDefinitionStatement functionDefinitionStmt = visitorCtx.scope().getFunction(funcName);
        if (functionDefinitionStmt == null) {
            visitorCtx.errorLog().addError(
                    funcCallCtx,
                    "function " + funcName + " does not exist"
            );
        } else {
            // check that the actual args are correct type
            List<FormalArgument> formalArgs = functionDefinitionStmt.getArgs();

            if (formalArgs.size() != actualArgs.size()) {
                visitorCtx.errorLog().addError(
                        funcCallCtx,
                        "wrong number of args for function call " + funcName + "." +
                                "expected " + formalArgs.size() + ", got " + actualArgs.size()
                );
            } else {
                for (int i = 0; i < actualArgs.size(); i++) {
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
                }
            }
        }

        return new FunctionStatement(funcName, actualArgs);
    }
}
