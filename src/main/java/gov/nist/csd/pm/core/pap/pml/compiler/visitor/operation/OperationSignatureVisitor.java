package gov.nist.csd.pm.core.pap.pml.compiler.visitor.operation;

import gov.nist.csd.pm.core.pap.operation.arg.type.Type;
import gov.nist.csd.pm.core.pap.operation.arg.type.VoidType;
import gov.nist.csd.pm.core.pap.operation.param.FormalParameter;
import gov.nist.csd.pm.core.pap.operation.reqcap.RequiredCapability;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser.AdminOpSignatureContext;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser.EventArgContext;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser.EventArgsContext;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser.FunctionSignatureContext;
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
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
        List<FormalParameter<?>> formalParameters = new FormalParameterListVisitor(visitorCtx)
            .visitOperationFormalParamList(ctx.operationFormalParamList());
        List<RequiredCapability> reqCaps = new ReqCapListVisitor(visitorCtx, formalParameters).visitReqCapList(ctx.reqCapList());
        List<FormalParameter<?>> eventParameters = parseEventParameters(ctx.eventArgs(), formalParameters, visitorCtx);

        writeArgsToScope(visitorCtx, formalParameters);

        PMLOperationSignature pmlOperationSignature = new PMLOperationSignature(
            OperationType.ADMINOP,
            funcName,
            returnType,
            formalParameters,
            eventParameters,
            reqCaps
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
        List<RequiredCapability> reqCaps = new ReqCapListVisitor(visitorCtx, args).visitReqCapList(ctx.reqCapList());
        List<FormalParameter<?>> eventParameters = parseEventParameters(ctx.eventArgs(), args, visitorCtx);

        writeArgsToScope(visitorCtx, args);

        PMLOperationSignature pmlOperationSignature = new PMLOperationSignature(
            OperationType.RESOURCEOP,
            funcName,
            returnType,
            args,
            eventParameters,
            reqCaps);

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
            args,
            List.of());

        addSignatureToCtx(visitorCtx, ctx, funcName, pmlRoutineSignature, addToCtx);

        return pmlRoutineSignature;
    }

    @Override
    public PMLOperationSignature visitFunctionSignature(FunctionSignatureContext ctx) {
        String funcName = ctx.ID().getText();
        Type<?> returnType = parseReturnType(ctx.returnType);
        List<FormalParameter<?>> args = new FormalParameterListVisitor(visitorCtx).visitFormalParamList(ctx.formalParamList());

        writeArgsToScope(visitorCtx, args);

        PMLOperationSignature pmlFunctionSignature = new PMLOperationSignature(
            OperationType.FUNCTION,
            funcName,
            returnType,
            args,
            List.of());

        addSignatureToCtx(visitorCtx, ctx, funcName, pmlFunctionSignature, addToCtx);

        return pmlFunctionSignature;
    }

    @Override
    public PMLOperationSignature visitQueryOpSignature(QueryOpSignatureContext ctx) {
        String funcName = ctx.ID().getText();
        Type<?> returnType = parseReturnType(ctx.returnType);
        List<FormalParameter<?>> args = new FormalParameterListVisitor(visitorCtx)
            .visitOperationFormalParamList(ctx.operationFormalParamList());
        List<RequiredCapability> reqCaps = new ReqCapListVisitor(visitorCtx, args).visitReqCapList(ctx.reqCapList());
        List<FormalParameter<?>> eventParameters = parseEventParameters(ctx.eventArgs(), args, visitorCtx);

        writeArgsToScope(visitorCtx, args);

        PMLOperationSignature pmlOperationSignature = new PMLOperationSignature(
            OperationType.QUERY,
            funcName,
            returnType,
            args,
            eventParameters,
            reqCaps);

        addSignatureToCtx(visitorCtx, ctx, funcName, pmlOperationSignature, addToCtx);

        return pmlOperationSignature;
    }

    private List<FormalParameter<?>> parseEventParameters(EventArgsContext ctx,
                                                      List<FormalParameter<?>> formalParams,
                                                      VisitorContext visitorCtx) {
        if (ctx == null) {
            return new ArrayList<>(formalParams);
        }

        Map<String, FormalParameter<?>> formalParameters = formalParams.stream()
            .collect(Collectors.toMap(FormalParameter::getName, fp -> fp));

        List<FormalParameter<?>> result = new ArrayList<>();
        Set<String> seen = new HashSet<>();

        for (EventArgContext eventArgCtx : ctx.eventArg()) {
            String name = eventArgCtx.ID().getText();
            if (seen.contains(name)) {
                visitorCtx.errorLog().addError(eventArgCtx,
                    String.format("duplicate event arg '%s'", name));
                continue;
            }
            seen.add(name);

            if (eventArgCtx.variableType() != null) { // new
                if (formalParameters.containsKey(name)) {
                    visitorCtx.errorLog().addError(eventArgCtx,
                        String.format("event arg already defined as formal parameter '%s'", name));
                    continue;
                }

                Type<?> type = TypeResolver.resolveFromParserCtx(eventArgCtx.variableType());
                result.add(new FormalParameter<>(name, type));
            } else { // existing
                FormalParameter<?> existing = formalParameters.get(name);
                if (existing == null) {
                    visitorCtx.errorLog().addError(eventArgCtx,
                        String.format("event arg '%s' has no type and does not match any formal parameter", name));
                    continue;
                }
                result.add(existing);
            }
        }

        return result;
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
