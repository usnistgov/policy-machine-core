package gov.nist.csd.pm.policy.pml.compiler.visitor;

import gov.nist.csd.pm.policy.exceptions.PMLFunctionNotDefinedException;
import gov.nist.csd.pm.policy.pml.antlr.PMLParser;
import gov.nist.csd.pm.policy.pml.antlr.PMLParserBaseVisitor;
import gov.nist.csd.pm.policy.pml.function.FormalArgument;
import gov.nist.csd.pm.policy.pml.function.FunctionSignature;
import gov.nist.csd.pm.policy.pml.model.context.VisitorContext;
import gov.nist.csd.pm.policy.pml.model.scope.FunctionAlreadyDefinedInScopeException;
import gov.nist.csd.pm.policy.pml.model.scope.PMLScopeException;
import gov.nist.csd.pm.policy.pml.model.scope.UnknownFunctionInScopeException;
import gov.nist.csd.pm.policy.pml.statement.ErrorStatement;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.pml.statement.PMLStatement;
import gov.nist.csd.pm.policy.pml.type.Type;

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
        String funcName = ctx.functionSignature().ID().getText();
        FunctionSignature signature;
        try {
            signature = visitorCtx.scope().getFunctionSignature(funcName);
        } catch (UnknownFunctionInScopeException e) {
            // this error will occur only if the function signature wasn't added to the scope during compilation
            // this happens before the function definition is visited, so if it's missing something went wrong
            // during compilation.
            visitorCtx.errorLog().addError(ctx, "signature for function " + funcName +
                    " was not compiled, check for errors during compilation");

            return new ErrorStatement(ctx);
        }

        List<PMLStatement> body = parseBody(ctx, signature.getArgs(), signature.getReturnType());
        if (body == null) {
            return new ErrorStatement(ctx);
        }

        FunctionDefinitionStatement functionDefinition = new FunctionDefinitionStatement.Builder(signature.getFunctionName())
                .returns(signature.getReturnType())
                .args(signature.getArgs())
                .body(body)
                .build();

        // add function to scope
        try {
            visitorCtx.scope().addFunction(functionDefinition);
        } catch (FunctionAlreadyDefinedInScopeException | PMLFunctionNotDefinedException e) {
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

        // add the args to the local scope, overwriting any variables with the same ID as the formal args
        for (FormalArgument formalArgument : args) {
            localVisitorCtx.scope().addOrOverwriteVariable(formalArgument.name(), formalArgument.type());
        }

        StatementBlockVisitor statementBlockVisitor = new StatementBlockVisitor(localVisitorCtx, returnType);
        StatementBlockVisitor.Result result = statementBlockVisitor.visitStatementBlock(ctx.statementBlock());

        if (!result.allPathsReturned() && !returnType.isVoid()) {
            visitorCtx.errorLog().addError(ctx, "not all conditional paths return");

            return null;
        }

        return result.stmts();
    }

    public static class FunctionSignatureVisitor extends PMLParserBaseVisitor<PMLStatement> {

        private VisitorContext visitorCtx;

        public FunctionSignatureVisitor(VisitorContext visitorCtx) {
            this.visitorCtx = visitorCtx;
        }

        @Override
        public PMLStatement visitFunctionSignature(PMLParser.FunctionSignatureContext ctx) {
            String funcName = ctx.ID().getText();
            List<FormalArgument> args = parseFormalArgs(ctx.formalArgList());
            if (args == null) {
                return new ErrorStatement(ctx);
            }

            Type returnType = parseReturnType(ctx.returnType);

            FunctionSignature signature = new FunctionSignature(funcName, returnType, args);

            try {
                visitorCtx.scope().addFunctionSignature(signature);
            } catch (FunctionAlreadyDefinedInScopeException e) {
                visitorCtx.errorLog().addError(ctx, e.getMessage());

                return new ErrorStatement(ctx);
            }

            return signature;
        }

        private List<FormalArgument> parseFormalArgs(PMLParser.FormalArgListContext formalArgListCtx) {
            List<FormalArgument> formalArguments = new ArrayList<>();
            Set<String> argNames = new HashSet<>();
            for (PMLParser.FormalArgContext formalArgCtx : formalArgListCtx.formalArg()) {
                String name = formalArgCtx.ID().getText();
                PMLParser.VariableTypeContext varTypeContext = formalArgCtx.variableType();

                // check that two formal args dont have the same name
                if (argNames.contains(name)) {
                    visitorCtx.errorLog().addError(
                            formalArgCtx,
                            String.format("formal arg '%s' already defined in signature", name)
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
}
