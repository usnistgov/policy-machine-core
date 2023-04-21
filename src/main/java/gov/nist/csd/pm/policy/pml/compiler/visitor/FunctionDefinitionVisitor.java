package gov.nist.csd.pm.policy.pml.compiler.visitor;

import gov.nist.csd.pm.policy.pml.antlr.PMLBaseVisitor;
import gov.nist.csd.pm.policy.pml.antlr.PMLParser;
import gov.nist.csd.pm.policy.pml.model.context.VisitorContext;
import gov.nist.csd.pm.policy.pml.model.expression.Type;
import gov.nist.csd.pm.policy.author.pal.model.scope.*;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.pml.statement.PALStatement;
import gov.nist.csd.pm.policy.pml.model.function.FormalArgument;
import gov.nist.csd.pm.policy.pml.statement.FunctionReturnStmt;
import gov.nist.csd.pm.policy.pml.model.scope.FunctionAlreadyDefinedInScopeException;
import gov.nist.csd.pm.policy.pml.model.scope.PALScopeException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FunctionDefinitionVisitor extends PMLBaseVisitor<FunctionDefinitionStatement> {

    private final VisitorContext visitorCtx;

    public FunctionDefinitionVisitor(VisitorContext visitorCtx) {
        this.visitorCtx = visitorCtx;
    }

    @Override
    public FunctionDefinitionStatement visitFuncDefStmt(PMLParser.FuncDefStmtContext ctx) {
        String funcName = ctx.VARIABLE_OR_FUNCTION_NAME().getText();
        List<FormalArgument> args = parseFormalArgs(ctx.formalArgList());
        Type returnType = parseReturnType(ctx.funcReturnType());
        List<PALStatement> body = parseBody(ctx, args);

        FunctionDefinitionStatement functionDefinition = new FunctionDefinitionStatement(funcName, returnType, args, body);

        // add function to scope
        try {
            visitorCtx.scope().addFunction(functionDefinition);
        } catch (FunctionAlreadyDefinedInScopeException e) {
            visitorCtx.errorLog().addError(ctx, e.getMessage());
        }

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
                Type retExprType = Type.any();
                try {
                    retExprType = returnStmt.getExpr().getType(visitorCtx.scope());
                } catch (PALScopeException e) {
                    visitorCtx.errorLog().addError(ctx, e.getMessage());
                }
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

    private List<PALStatement> parseBody(PMLParser.FuncDefStmtContext ctx, List<FormalArgument> args) {
        PMLParser.FuncBodyContext funcBodyCtx = ctx.funcBody();

        // create a new scope for the function body
        VisitorContext localVisitorCtx = visitorCtx.copy();
        // add the args to the local scope
        for (FormalArgument formalArgument : args) {
            // string literal as a placeholder since the actual value is not determined yet
            try {
                localVisitorCtx.scope().addVariable(formalArgument.name(), formalArgument.type(), false);
            } catch (PALScopeException e) {
                visitorCtx.errorLog().addError(ctx, e.getMessage());
            }
        }

        StatementVisitor statementVisitor = new StatementVisitor(localVisitorCtx);
        List<PALStatement> stmts = new ArrayList<>();
        for (PMLParser.StmtContext stmtCtx : funcBodyCtx.stmt()) {
            stmts.add(statementVisitor.visitStmt(stmtCtx));
        }

        return stmts;
    }

    private List<FormalArgument> parseFormalArgs(PMLParser.FormalArgListContext formalArgListCtx) {
        List<FormalArgument> formalArguments = new ArrayList<>();
        Set<String> argNames = new HashSet<>();
        for (PMLParser.FormalArgContext formalArgCtx : formalArgListCtx.formalArg()) {
            String name = formalArgCtx.VARIABLE_OR_FUNCTION_NAME().getText();
            PMLParser.VarTypeContext varTypeContext = formalArgCtx.formalArgType().varType();

            // check that a formalArg does not clash with an already defined variable
            if (visitorCtx.scope().variableExists(name) || argNames.contains(name)) {
                visitorCtx.errorLog().addError(
                        formalArgCtx,
                        String.format("formal arg '%s' already defined in scope", name)
                );
            }

            Type type = Type.toType(varTypeContext);

            argNames.add(name);
            formalArguments.add(new FormalArgument(name, type));
        }

        return formalArguments;
    }

    private Type parseReturnType(PMLParser.FuncReturnTypeContext funcReturnTypeCtx) {
        if (funcReturnTypeCtx == null) {
            return Type.voidType();
        }

        if (funcReturnTypeCtx instanceof PMLParser.VarReturnTypeContext varReturnTypeCtx) {
            PMLParser.VarTypeContext varTypeCtx = varReturnTypeCtx.varType();
            return Type.toType(varTypeCtx);
        } else if (funcReturnTypeCtx instanceof PMLParser.VoidReturnTypeContext) {
            return Type.voidType();
        }

        return Type.any();
    }
}
