package gov.nist.csd.pm.policy.pml.compiler.visitor;

import gov.nist.csd.pm.policy.pml.antlr.PMLParser;
import gov.nist.csd.pm.policy.pml.compiler.Variable;
import gov.nist.csd.pm.policy.pml.function.FormalArgument;
import gov.nist.csd.pm.policy.pml.function.FunctionSignature;
import gov.nist.csd.pm.policy.pml.context.VisitorContext;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.pml.statement.PMLStatement;
import gov.nist.csd.pm.policy.pml.type.Type;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FunctionDefinitionVisitor extends PMLBaseVisitor<FunctionDefinitionStatement> {

    public FunctionDefinitionVisitor(VisitorContext visitorCtx) {
        super(visitorCtx);
    }

    @Override
    public FunctionDefinitionStatement visitFunctionDefinitionStatement(PMLParser.FunctionDefinitionStatementContext ctx) {
        FunctionSignature signature = new FunctionSignatureVisitor(visitorCtx).visitFunctionSignature(ctx.functionSignature());

        List<PMLStatement> body = parseBody(ctx, signature.getArgs(), signature.getReturnType());
        if (body == null) {
            return new FunctionDefinitionStatement(ctx);
        }


        return new FunctionDefinitionStatement.Builder(signature.getFunctionName())
                .returns(signature.getReturnType())
                .args(signature.getArgs())
                .body(body)
                .build();
    }

    private List<PMLStatement> parseBody(PMLParser.FunctionDefinitionStatementContext ctx,
                                         List<FormalArgument> args,
                                         Type returnType) {
        // create a new scope for the function body
        VisitorContext localVisitorCtx = visitorCtx.copy();

        // add the args to the local scope, overwriting any variables with the same ID as the formal args
        for (FormalArgument formalArgument : args) {
            localVisitorCtx.scope().addOrOverwriteVariable(formalArgument.name(), new Variable(formalArgument.name(), formalArgument.type(), false));
        }

        StatementBlockVisitor statementBlockVisitor = new StatementBlockVisitor(localVisitorCtx, returnType);
        StatementBlockVisitor.Result result = statementBlockVisitor.visitStatementBlock(ctx.statementBlock());

        if (!result.allPathsReturned() && !returnType.isVoid()) {
            visitorCtx.errorLog().addError(ctx, "not all conditional paths return");

            return null;
        }

        return result.stmts();
    }

    public static class FunctionSignatureVisitor extends PMLBaseVisitor<PMLStatement> {

        public FunctionSignatureVisitor(VisitorContext visitorCtx) {
            super(visitorCtx);
        }

        @Override
        public FunctionSignature visitFunctionSignature(PMLParser.FunctionSignatureContext ctx) {
            String funcName = ctx.ID().getText();
            List<FormalArgument> args = parseFormalArgs(ctx.formalArgList());
            if (args == null) {
                return new FunctionSignature(ctx);
            }

            Type returnType = parseReturnType(ctx.returnType);

            return new FunctionSignature(funcName, returnType, args);
        }

        private List<FormalArgument> parseFormalArgs(PMLParser.FormalArgListContext formalArgListCtx) {
            List<FormalArgument> formalArguments = new ArrayList<>();
            Set<String> argNames = new HashSet<>();
            for (PMLParser.FormalArgContext formalArgCtx : formalArgListCtx.formalArg()) {
                String name = formalArgCtx.ID().getText();
                PMLParser.VariableTypeContext varTypeContext = formalArgCtx.variableType();

                // check that two formal args dont have the same name and that there are no constants with the same name
                if (argNames.contains(name)) {
                    visitorCtx.errorLog().addError(
                            formalArgCtx,
                            String.format("formal arg '%s' already defined in signature", name)
                    );

                    return null;
                } else if (visitorCtx.scope().variableExists(name)) {
                    visitorCtx.errorLog().addError(
                            formalArgCtx,
                            String.format("formal arg '%s' already defined as a constant in scope", name)
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
