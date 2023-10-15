package gov.nist.csd.pm.policy.pml.compiler.visitor;

import gov.nist.csd.pm.policy.pml.antlr.PMLParserBaseVisitor;
import gov.nist.csd.pm.policy.pml.antlr.PMLParser;
import gov.nist.csd.pm.policy.pml.model.context.VisitorContext;
import gov.nist.csd.pm.policy.pml.statement.ErrorStatement;
import gov.nist.csd.pm.policy.pml.type.Type;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.pml.statement.PMLStatement;
import gov.nist.csd.pm.policy.pml.function.FormalArgument;
import gov.nist.csd.pm.policy.pml.model.scope.FunctionAlreadyDefinedInScopeException;
import gov.nist.csd.pm.policy.pml.model.scope.PMLScopeException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FunctionDefinitionVisitor extends PMLParserBaseVisitor<PMLStatement> {

    private final VisitorContext visitorCtx;

    public FunctionDefinitionVisitor(VisitorContext visitorCtx) {
        this.visitorCtx = visitorCtx;
    }

    @Override
    public PMLStatement visitFunctionDefinitionStatement(PMLParser.FunctionDefinitionStatementContext ctx) {
        String funcName = ctx.ID().getText();
        List<FormalArgument> args = parseFormalArgs(ctx.formalArgList());
        if (args == null) {
            return new ErrorStatement(ctx);
        }

        Type returnType = parseReturnType(ctx.returnType);

        List<PMLStatement> body = parseBody(ctx, args, returnType);
        if (body == null) {
            return new ErrorStatement(ctx);
        }

        FunctionDefinitionStatement functionDefinition = new FunctionDefinitionStatement.Builder(funcName)
                .returns(returnType)
                .args(args)
                .body(body)
                .build();

        // add function to scope
        try {
            visitorCtx.scope().addFunction(functionDefinition);
        } catch (FunctionAlreadyDefinedInScopeException e) {
            visitorCtx.errorLog().addError(ctx, e.getMessage());

            return new ErrorStatement(ctx);
        }

        return functionDefinition;
    }

    private List<PMLStatement> parseBody(PMLParser.FunctionDefinitionStatementContext ctx,
                                         List<FormalArgument> args,
                                         Type returnType) {
        // create a new scope for the function body
        VisitorContext localVisitorCtx = visitorCtx.copy();
        // add the args to the local scope
        for (FormalArgument formalArgument : args) {
            try {
                localVisitorCtx.scope().addVariable(formalArgument.name(), formalArgument.type(), false);
            } catch (PMLScopeException e) {
                visitorCtx.errorLog().addError(ctx, e.getMessage());

                return null;
            }
        }

        StatementBlockVisitor statementBlockVisitor = new StatementBlockVisitor(localVisitorCtx, returnType);
        StatementBlockVisitor.Result result = statementBlockVisitor.visitStatementBlock(ctx.statementBlock());

        if (!result.allPathsReturned() && !returnType.isVoid()) {
            visitorCtx.errorLog().addError(ctx, "not all conditional paths return");

            return null;
        }

        return result.stmts();
    }

    private List<FormalArgument> parseFormalArgs(PMLParser.FormalArgListContext formalArgListCtx) {
        List<FormalArgument> formalArguments = new ArrayList<>();
        Set<String> argNames = new HashSet<>();
        for (PMLParser.FormalArgContext formalArgCtx : formalArgListCtx.formalArg()) {
            String name = formalArgCtx.ID().getText();
            PMLParser.VariableTypeContext varTypeContext = formalArgCtx.variableType();

            // check that a formalArg does not clash with an already defined variable
            if (visitorCtx.scope().variableExists(name) || argNames.contains(name)) {
                visitorCtx.errorLog().addError(
                        formalArgCtx,
                        String.format("formal arg '%s' already defined in scope", name)
                );

                return null;
            }

            Type type = Type.toType(varTypeContext);

            argNames.add(name);
            formalArguments.add(new FormalArgument(name, type));
        }

        return formalArguments;
    }

    private Type parseReturnType(PMLParser.VariableTypeContext variableTypeContext) {
        if (variableTypeContext == null) {
            return Type.voidType();
        }

        return Type.toType(variableTypeContext);
    }
}
