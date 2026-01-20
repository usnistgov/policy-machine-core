package gov.nist.csd.pm.core.pap.pml.compiler.visitor.operation;

import gov.nist.csd.pm.core.pap.operation.arg.type.Type;
import gov.nist.csd.pm.core.pap.operation.arg.type.VoidType;
import gov.nist.csd.pm.core.pap.operation.param.FormalParameter;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser.AdminOpSignatureContext;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser.BasicFunctionSignatureContext;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser.QueryOpSignatureContext;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser.ResourceOpSignatureContext;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser.RoutineSignatureContext;
import gov.nist.csd.pm.core.pap.pml.compiler.Variable;
import gov.nist.csd.pm.core.pap.pml.compiler.visitor.PMLBaseVisitor;
import gov.nist.csd.pm.core.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.core.pap.pml.operation.PMLOperationSignature;
import gov.nist.csd.pm.core.pap.pml.operation.PMLOperationSignature.OperationType;
import gov.nist.csd.pm.core.pap.pml.scope.OperationAlreadyDefinedInScopeException;
import gov.nist.csd.pm.core.pap.pml.type.TypeResolver;
import java.util.List;
import org.antlr.v4.runtime.ParserRuleContext;

public class OperationSignatureVisitor extends PMLBaseVisitor<PMLOperationSignature> {

    private boolean addToCtx;

    public OperationSignatureVisitor(VisitorContext visitorCtx, boolean addToCtx) {
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
            OperationType.ADMINOP,
            funcName,
            returnType,
            args
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
            OperationType.RESOURCEOP,
            funcName,
            returnType,
            args
        );

        addSignatureToCtx(visitorCtx, ctx, funcName, pmlOperationSignature, addToCtx);

        return pmlOperationSignature;
    }

    @Override
    public PMLOperationSignature visitRoutineSignature(RoutineSignatureContext ctx) {
        String funcName = ctx.ID().getText();
        Type<?> returnType = parseReturnType(ctx.returnType);
        List<FormalParameter<?>> args = new FormalParameterListVisitor(visitorCtx).visitFormalParamList(ctx.formalParamList());

        writeArgsToScope(visitorCtx, args);

        PMLOperationSignature pmlRoutineSignature = new PMLOperationSignature(
            OperationType.ROUTINE,
            funcName,
            returnType,
            args
        );

        addSignatureToCtx(visitorCtx, ctx, funcName, pmlRoutineSignature, addToCtx);

        return pmlRoutineSignature;
    }

    @Override
    public PMLOperationSignature visitBasicFunctionSignature(BasicFunctionSignatureContext ctx) {
        String funcName = ctx.ID().getText();
        Type<?> returnType = parseReturnType(ctx.returnType);
        List<FormalParameter<?>> args = new FormalParameterListVisitor(visitorCtx).visitFormalParamList(ctx.formalParamList());

        writeArgsToScope(visitorCtx, args);

        PMLOperationSignature pmlFunctionSignature = new PMLOperationSignature(
            OperationType.FUNCTION,
            funcName,
            returnType,
            args
        );

        addSignatureToCtx(visitorCtx, ctx, funcName, pmlFunctionSignature, addToCtx);

        return pmlFunctionSignature;
    }

    @Override
    public PMLOperationSignature visitQueryOpSignature(QueryOpSignatureContext ctx) {
        String funcName = ctx.ID().getText();
        Type<?> returnType = parseReturnType(ctx.returnType);
        List<FormalParameter<?>> args = new FormalParameterListVisitor(visitorCtx)
            .visitOperationFormalParamList(ctx.operationFormalParamList());

        writeArgsToScope(visitorCtx, args);

        PMLOperationSignature pmlOperationSignature = new PMLOperationSignature(
            OperationType.QUERY,
            funcName,
            returnType,
            args
        );

        addSignatureToCtx(visitorCtx, ctx, funcName, pmlOperationSignature, addToCtx);

        return pmlOperationSignature;
    }

    private <U extends ParserRuleContext> void addSignatureToCtx(VisitorContext visitorCtx,
                                                                 U ctx,
                                                                 String funcName,
                                                                 PMLOperationSignature signature, boolean addToCtx) {
        if (!addToCtx) {
            return;
        }

        try {
            visitorCtx.scope().addOperation(funcName, signature);
        } catch (OperationAlreadyDefinedInScopeException e) {
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
