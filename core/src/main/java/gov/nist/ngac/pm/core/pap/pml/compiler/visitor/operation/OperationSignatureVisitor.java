/*
 * This Software (Policy Machine) is being made available as a public service by the
 * National Institute of Standards and Technology (NIST), an Agency of the United
 * States Department of Commerce. This software was developed in part by employees of
 * NIST and in part by NIST contractors. Copyright in portions of this software that
 * were developed by NIST contractors has been licensed or assigned to NIST. Pursuant
 * to Title 17 United States Code Section 105, works of NIST employees are not
 * subject to copyright protection in the United States. However, NIST may hold
 * international copyright in software created by its employees and domestic
 * copyright (or licensing rights) in portions of software that were assigned or
 * licensed to NIST. To the extent that NIST holds copyright in this software, it is
 * being made available under the Creative Commons Attribution 4.0 International
 * license (CC BY 4.0). The disclaimers of the CC BY 4.0 license apply to all parts
 * of the software developed or licensed by NIST.
 *
 * ACCESS THE FULL CC BY 4.0 LICENSE HERE:
 * https://creativecommons.org/licenses/by/4.0/legalcode
 */

package gov.nist.ngac.pm.core.pap.pml.compiler.visitor.operation;

import static gov.nist.ngac.pm.core.pap.operation.arg.type.BasicTypes.VOID_TYPE;

import gov.nist.ngac.pm.core.pap.operation.arg.type.Type;
import gov.nist.ngac.pm.core.pap.operation.arg.type.VoidType;
import gov.nist.ngac.pm.core.pap.operation.param.FormalParameter;
import gov.nist.ngac.pm.core.pap.operation.reqcap.RequiredCapability;
import gov.nist.ngac.pm.core.pap.pml.antlr.PMLParser;
import gov.nist.ngac.pm.core.pap.pml.antlr.PMLParser.AdminOpSignatureContext;
import gov.nist.ngac.pm.core.pap.pml.antlr.PMLParser.AnnotationsContext;
import gov.nist.ngac.pm.core.pap.pml.antlr.PMLParser.EventCtxAnnotationContext;
import gov.nist.ngac.pm.core.pap.pml.antlr.PMLParser.EventCtxArgContext;
import gov.nist.ngac.pm.core.pap.pml.antlr.PMLParser.FunctionSignatureContext;
import gov.nist.ngac.pm.core.pap.pml.antlr.PMLParser.QueryOpSignatureContext;
import gov.nist.ngac.pm.core.pap.pml.antlr.PMLParser.ReqCapAnnotationContext;
import gov.nist.ngac.pm.core.pap.pml.antlr.PMLParser.ReqCapContext;
import gov.nist.ngac.pm.core.pap.pml.antlr.PMLParser.ResourceOpSignatureContext;
import gov.nist.ngac.pm.core.pap.pml.antlr.PMLParser.RoutineSignatureContext;
import gov.nist.ngac.pm.core.pap.pml.compiler.Variable;
import gov.nist.ngac.pm.core.pap.pml.compiler.visitor.PMLBaseVisitor;
import gov.nist.ngac.pm.core.pap.pml.context.VisitorContext;
import gov.nist.ngac.pm.core.pap.pml.exception.PMLCompilationRuntimeException;
import gov.nist.ngac.pm.core.pap.pml.operation.PMLOperationSignature;
import gov.nist.ngac.pm.core.pap.pml.operation.PMLOperationSignature.OperationType;
import gov.nist.ngac.pm.core.pap.pml.operation.PMLRequiredCapabilityFunc;
import gov.nist.ngac.pm.core.pap.pml.scope.OperationAlreadyDefinedInScopeException;
import gov.nist.ngac.pm.core.pap.pml.statement.PMLStatementBlock;
import gov.nist.ngac.pm.core.pap.pml.type.TypeResolver;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.antlr.v4.runtime.ParserRuleContext;

/**
 * Compiles an operation's signature into a {@link PMLOperationSignature}.
 */
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
        Annotations annotations = parseAnnotations(ctx.annotations(), formalParameters);
        List<RequiredCapability> reqCaps = annotations.requiredCapabilities();
        List<FormalParameter<?>> eventParameters = annotations.eventParameters();

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
        Annotations annotations = parseAnnotations(ctx.annotations(), args);
        List<RequiredCapability> reqCaps = annotations.requiredCapabilities();
        List<FormalParameter<?>> eventParameters = annotations.eventParameters();

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
        Annotations annotations = parseAnnotations(ctx.annotations(), args);
        List<RequiredCapability> reqCaps = annotations.requiredCapabilities();
        List<FormalParameter<?>> eventParameters = annotations.eventParameters();

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

    private record Annotations(List<FormalParameter<?>> eventParameters, List<RequiredCapability> requiredCapabilities) {}

    private Annotations parseAnnotations(List<AnnotationsContext> annotations, List<FormalParameter<?>> formalParams) {
        List<FormalParameter<?>> eventParams = null;
        List<RequiredCapability> reqCaps = new ArrayList<>();

        for (AnnotationsContext annotationsContext : annotations) {
            if (annotationsContext instanceof EventCtxAnnotationContext eventCtxAnnotationCtx) {
                if (eventParams != null) {
                    throw new PMLCompilationRuntimeException("only one @EventCtx annotation allowed");
                }

                eventParams = parseEventParameters(eventCtxAnnotationCtx, formalParams, visitorCtx);
            } else if (annotationsContext instanceof ReqCapAnnotationContext reqCapAnnotationCtx) {
                RequiredCapability requiredCapability = parseReqCap(reqCapAnnotationCtx.reqCap(), formalParams);
                reqCaps.add(requiredCapability);
            }
        }

        return new Annotations(eventParams, reqCaps);
    }

    private RequiredCapability parseReqCap(ReqCapContext ctx, List<FormalParameter<?>> formalParams) {
        PMLStatementBlock pmlStatementBlock = StatementBlockParser.parseBasicStatementBlock(
            visitorCtx,
            ctx.basicStatementBlock(),
            VOID_TYPE,
            formalParams,
            true
        );

        return new PMLRequiredCapabilityFunc(pmlStatementBlock);
    }

    private List<FormalParameter<?>> parseEventParameters(EventCtxAnnotationContext ctx,
                                                      List<FormalParameter<?>> formalParams,
                                                      VisitorContext visitorCtx) {
        if (ctx == null) {
            return new ArrayList<>(formalParams);
        }

        Map<String, FormalParameter<?>> formalParameters = formalParams.stream()
            .collect(Collectors.toMap(FormalParameter::getName, fp -> fp));

        List<FormalParameter<?>> result = new ArrayList<>();
        Set<String> seen = new HashSet<>();

        for (EventCtxArgContext eventArgCtx : ctx.eventCtxArgs().eventCtxArg()) {
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
                result.add(new FormalParameter<>(name, type, eventArgCtx.OPTIONAL_PARAM() == null));
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
