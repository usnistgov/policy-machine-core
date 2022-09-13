package gov.nist.csd.pm.policy.author.pal.compiler.visitor;

import gov.nist.csd.pm.policy.author.pal.antlr.PALBaseVisitor;
import gov.nist.csd.pm.policy.author.pal.antlr.PALParser;
import gov.nist.csd.pm.policy.author.pal.model.context.VisitorContext;
import gov.nist.csd.pm.policy.author.pal.model.expression.Type;
import gov.nist.csd.pm.policy.author.pal.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.author.pal.statement.PALStatement;
import gov.nist.csd.pm.policy.author.pal.model.function.FormalArgument;
import gov.nist.csd.pm.policy.author.pal.statement.FunctionReturnStmt;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FunctionDefinitionVisitor extends PALBaseVisitor<FunctionDefinitionStatement> {

    private final VisitorContext visitorCtx;

    public FunctionDefinitionVisitor(VisitorContext visitorCtx) {
        this.visitorCtx = visitorCtx;
    }

    @Override
    public FunctionDefinitionStatement visitFuncDefStmt(PALParser.FuncDefStmtContext ctx) {
        String funcName = ctx.IDENTIFIER().getText();
        if (visitorCtx.scope().getFunction(funcName) != null) {
            visitorCtx.errorLog().addError(
                    ctx,
                    "function " + funcName + " already defined in scope"
            );
        }

        List<FormalArgument> args = parseFormalArgs(ctx.formalArgList());
        Type returnType = parseReturnType(ctx.funcReturnType());
        List<PALStatement> body = parseBody(ctx, args);

        FunctionDefinitionStatement functionDefinition = new FunctionDefinitionStatement(funcName, returnType, args, body);

        // add function to scope
        visitorCtx.scope().addFunction(ctx, functionDefinition);

        // check that the body has a return statement IF the return type is NOT VOID
        PALStatement lastStmt = null;
        if (body.size() > 0) {
            lastStmt = body.get(body.size()-1);
        }

        if (returnType.isVoid()) {
            if (lastStmt instanceof FunctionReturnStmt returnStmt) {
                if (!returnStmt.isVoid()) {
                    visitorCtx.errorLog().addError(
                            ctx,
                            "return statement should be empty for functions that return VOID"
                    );
                }
            }
        } else {
            if (lastStmt instanceof FunctionReturnStmt returnStmt) {
                Type retExprType = returnStmt.getExpr().getType(visitorCtx.scope());
                if (returnStmt.isVoid()) {
                     visitorCtx.errorLog().addError(
                             ctx,
                             "return statement missing expression"
                     );
                 } else if (!retExprType.equals(returnType)) {
                     visitorCtx.errorLog().addError(
                             ctx,
                             "function expected to return type " + returnType + " not " + retExprType
                     );
                 }
            } else {
                visitorCtx.errorLog().addError(
                        ctx,
                        "function missing return statement at end of function body"
                );
            }
        }

        return functionDefinition;
    }

    private List<PALStatement> parseBody(PALParser.FuncDefStmtContext ctx, List<FormalArgument> args) {
        PALParser.FuncBodyContext funcBodyCtx = ctx.funcBody();

        // create a new scope for the function body
        VisitorContext localVisitorCtx = visitorCtx.copy();
        // add the args to the local scope
        for (FormalArgument formalArgument : args) {
            // string literal as a placeholder since the actual value is not determined yet
            localVisitorCtx.scope().addVariable(formalArgument.name(), formalArgument.type(), false);
        }

        StatementVisitor statementVisitor = new StatementVisitor(localVisitorCtx);
        List<PALStatement> stmts = new ArrayList<>();
        for (PALParser.StmtContext stmtCtx : funcBodyCtx.stmt()) {
            stmts.add(statementVisitor.visitStmt(stmtCtx));
        }

        return stmts;
    }

    private List<FormalArgument> parseFormalArgs(PALParser.FormalArgListContext formalArgListCtx) {
        List<FormalArgument> formalArguments = new ArrayList<>();
        Set<String> argNames = new HashSet<>();
        for (PALParser.FormalArgContext formalArgCtx : formalArgListCtx.formalArg()) {
            String name = formalArgCtx.IDENTIFIER().getText();
            PALParser.VarTypeContext varTypeContext = formalArgCtx.formalArgType().varType();

            // check that a formalArg does not clash with an already defined variable
            if (visitorCtx.scope().getVariable(name) != null || argNames.contains(name)) {
                visitorCtx.errorLog().addError(
                        formalArgCtx,
                        "formal arg " + name + " already defined in scope"
                );
            }

            Type type = Type.toType(varTypeContext);

            argNames.add(name);
            formalArguments.add(new FormalArgument(name, type));
        }

        return formalArguments;
    }

    private Type parseReturnType(PALParser.FuncReturnTypeContext funcReturnTypeCtx) {
        if (funcReturnTypeCtx == null) {
            return Type.voidType();
        }

        if (funcReturnTypeCtx instanceof PALParser.VarReturnTypeContext varReturnTypeCtx) {
            PALParser.VarTypeContext varTypeCtx = varReturnTypeCtx.varType();
            return Type.toType(varTypeCtx);
        } else if (funcReturnTypeCtx instanceof PALParser.VoidReturnTypeContext) {
            return Type.voidType();
        }

        return Type.any();
    }
}
