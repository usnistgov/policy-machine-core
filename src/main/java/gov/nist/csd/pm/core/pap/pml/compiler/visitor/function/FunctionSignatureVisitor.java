package gov.nist.csd.pm.core.pap.pml.compiler.visitor.function;

import static gov.nist.csd.pm.core.pap.function.arg.type.BasicTypes.VOID_TYPE;

import gov.nist.csd.pm.core.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.core.pap.function.arg.type.Type;
import gov.nist.csd.pm.core.pap.function.arg.type.VoidType;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser.AdminOpSignatureContext;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser.BasicFunctionSignatureContext;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser.ResourceOpSignatureContext;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser.RoutineSignatureContext;
import gov.nist.csd.pm.core.pap.pml.compiler.Variable;
import gov.nist.csd.pm.core.pap.pml.compiler.visitor.PMLBaseVisitor;
import gov.nist.csd.pm.core.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.core.pap.pml.function.PMLFunctionSignature;
import gov.nist.csd.pm.core.pap.pml.function.basic.PMLBasicFunctionSignature;
import gov.nist.csd.pm.core.pap.pml.function.operation.PMLOperationSignature;
import gov.nist.csd.pm.core.pap.pml.function.routine.PMLRoutineSignature;
import gov.nist.csd.pm.core.pap.pml.scope.FunctionAlreadyDefinedInScopeException;
import gov.nist.csd.pm.core.pap.pml.type.TypeResolver;
import java.util.List;
import org.antlr.v4.runtime.ParserRuleContext;

public class FunctionSignatureVisitor extends PMLBaseVisitor<PMLFunctionSignature> {

    private boolean addToCtx;

    public FunctionSignatureVisitor(VisitorContext visitorCtx, boolean addToCtx) {
        super(visitorCtx);
        this.addToCtx = addToCtx;
    }

    public boolean isAddToCtx() {
        return addToCtx;
    }

    public void setAddToCtx(boolean addToCtx) {
        this.addToCtx = addToCtx;
    }

    @Override
    public PMLOperationSignature visitAdminOpSignature(AdminOpSignatureContext ctx) {
        String funcName = ctx.ID().getText();
        Type<?> returnType = parseReturnType(ctx.returnType);
        List<FormalParameter<?>> args = new FormalParameterListVisitor(visitorCtx)
            .visitOperationFormalParamList(ctx.operationFormalParamList());

        writeArgsToScope(visitorCtx, args);

        PMLOperationSignature pmlOperationSignature = new PMLOperationSignature(
            funcName,
            returnType,
            args,
            true
        );

        addSignatureToCtx(visitorCtx, ctx, funcName, pmlOperationSignature, addToCtx);

        return pmlOperationSignature;
    }

    @Override
    public PMLOperationSignature visitResourceOpSignature(ResourceOpSignatureContext ctx) {
        String funcName = ctx.ID().getText();
        Type<?> returnType = parseReturnType(ctx.returnType);
        List<FormalParameter<?>> args = new FormalParameterListVisitor(visitorCtx)
            .visitOperationFormalParamList(ctx.operationFormalParamList());

        writeArgsToScope(visitorCtx, args);

        PMLOperationSignature pmlOperationSignature = new PMLOperationSignature(
            funcName,
            returnType,
            args,
            false
        );

        addSignatureToCtx(visitorCtx, ctx, funcName, pmlOperationSignature, addToCtx);

        return pmlOperationSignature;
    }

    @Override
    public PMLRoutineSignature visitRoutineSignature(RoutineSignatureContext ctx) {
        String funcName = ctx.ID().getText();
        Type<?> returnType = parseReturnType(ctx.returnType);
        List<FormalParameter<?>> args = new FormalParameterListVisitor(visitorCtx).visitFormalParamList(ctx.formalParamList());

        writeArgsToScope(visitorCtx, args);

        PMLRoutineSignature pmlRoutineSignature = new PMLRoutineSignature(
            funcName,
            returnType,
            args
        );

        addSignatureToCtx(visitorCtx, ctx, funcName, pmlRoutineSignature, addToCtx);

        return pmlRoutineSignature;
    }

    @Override
    public PMLBasicFunctionSignature visitBasicFunctionSignature(BasicFunctionSignatureContext ctx) {
        String funcName = ctx.ID().getText();
        Type<?> returnType = parseReturnType(ctx.returnType);
        List<FormalParameter<?>> args = new FormalParameterListVisitor(visitorCtx).visitFormalParamList(ctx.formalParamList());

        writeArgsToScope(visitorCtx, args);

        PMLBasicFunctionSignature pmlFunctionSignature = new PMLBasicFunctionSignature(
            funcName,
            returnType,
            args
        );

        addSignatureToCtx(visitorCtx, ctx, funcName, pmlFunctionSignature, addToCtx);

        return pmlFunctionSignature;
    }

    private <U extends ParserRuleContext> void addSignatureToCtx(VisitorContext visitorCtx,
                                                                 U ctx,
                                                                 String funcName,
                                                                 PMLFunctionSignature signature, boolean addToCtx) {
        if (!addToCtx) {
            return;
        }

        try {
            visitorCtx.scope().addFunction(funcName, signature);
        } catch (FunctionAlreadyDefinedInScopeException e) {
            visitorCtx.errorLog().addError(ctx, e.getMessage());
        }
    }

    private void writeArgsToScope(VisitorContext visitorCtx, List<FormalParameter<?>> args) {
        // write args to scope for compiling check block
        VisitorContext copy = visitorCtx.copy();
        for (FormalParameter<?> formParam : args) {
            copy.scope().updateVariable(
                formParam.getName(),
                new Variable(formParam.getName(), formParam.getType(), false)
            );
        }
    }

    private Type<?> parseReturnType(PMLParser.VariableTypeContext variableTypeContext) {
        if (variableTypeContext == null) {
            return new VoidType();
        }

        return TypeResolver.resolveFromParserCtx(variableTypeContext);
    }
}
