package gov.nist.csd.pm.core.pap.pml.compiler.visitor.operation;

import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.LONG_TYPE;
import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.STRING_TYPE;

import gov.nist.csd.pm.core.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.core.pap.operation.arg.type.ListType;
import gov.nist.csd.pm.core.pap.operation.arg.type.Type;
import gov.nist.csd.pm.core.pap.operation.param.FormalParameter;
import gov.nist.csd.pm.core.pap.operation.param.NodeIdFormalParameter;
import gov.nist.csd.pm.core.pap.operation.param.NodeIdListFormalParameter;
import gov.nist.csd.pm.core.pap.operation.param.NodeNameFormalParameter;
import gov.nist.csd.pm.core.pap.operation.param.NodeNameListFormalParameter;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser.NodeArgAnnotationContext;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser.OperationFormalParamContext;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser.OperationFormalParamListContext;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser.StringLitContext;
import gov.nist.csd.pm.core.pap.pml.compiler.visitor.ExpressionVisitor;
import gov.nist.csd.pm.core.pap.pml.compiler.visitor.PMLBaseVisitor;
import gov.nist.csd.pm.core.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.core.pap.pml.exception.PMLCompilationRuntimeException;
import gov.nist.csd.pm.core.pap.pml.type.TypeResolver;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.antlr.v4.runtime.ParserRuleContext;

public class FormalParameterListVisitor extends PMLBaseVisitor<List<FormalParameter<?>>> {

    public FormalParameterListVisitor(VisitorContext visitorCtx) {
        super(visitorCtx);
    }

    @Override
    public List<FormalParameter<?>> visitOperationFormalParamList(OperationFormalParamListContext ctx) {
        List<FormalParameter<?>> params = new ArrayList<>();
        Set<String> paramNames = new HashSet<>();

        for (OperationFormalParamContext operationFormalParamContext : ctx.operationFormalParam()) {
            String name = operationFormalParamContext.ID().getText();
            validateParamName(paramNames, name, operationFormalParamContext);

            Type<?> type = TypeResolver.resolveFromParserCtx(operationFormalParamContext.variableType());

            boolean isNodeOp = operationFormalParamContext.nodeArgAnnotation() != null;
            if (isNodeOp) {
                AccessRightSet reqCaps = parseReqCaps(operationFormalParamContext.nodeArgAnnotation());

                // node params can be one of 4 types: int64, int64[], string, string[]
                if (type.equals(LONG_TYPE)) {
                    params.add(new NodeIdFormalParameter(name, reqCaps));
                } else if (type.equals(ListType.of(LONG_TYPE))) {
                    params.add(new NodeIdListFormalParameter(name, reqCaps));
                } else if (type.equals(STRING_TYPE)) {
                    params.add(new NodeNameFormalParameter(name, reqCaps));
                } else if (type.equals(ListType.of(STRING_TYPE))) {
                    params.add(new NodeNameListFormalParameter(name, reqCaps));
                } else {
                    throw new PMLCompilationRuntimeException(operationFormalParamContext, "@node annotation cannot be applied to type " + type);
                }
            } else {
                params.add(new FormalParameter<>(name, type));
            }

            paramNames.add(name);
        }

        return params;
    }

    private AccessRightSet parseReqCaps(NodeArgAnnotationContext nodeArgAnnotationContext) {
        return new AccessRightSet(parseStringArrayLit(nodeArgAnnotationContext.stringLit()));
    }

    @Override
    public List<FormalParameter<?>> visitFormalParamList(PMLParser.FormalParamListContext ctx) {
        List<FormalParameter<?>> params = new ArrayList<>();
        Set<String> paramNames = new HashSet<>();
        for (int i = 0; i < ctx.formalParam().size(); i++) {
            PMLParser.FormalParamContext formalArgCtx = ctx.formalParam().get(i);
            String name = formalArgCtx.ID().getText();
            validateParamName(paramNames, name, formalArgCtx);

            // get arg type
            PMLParser.VariableTypeContext varTypeContext = formalArgCtx.variableType();
            Type<?> type = TypeResolver.resolveFromParserCtx(varTypeContext);

            params.add(new FormalParameter<>(name, type));
            paramNames.add(name);
        }

        return params;
    }

    private void validateParamName(Set<String> paramNames, String name, ParserRuleContext ctx) {
        if (paramNames.contains(name) || visitorCtx.scope().getConstants().containsKey(name)) {
            throw new PMLCompilationRuntimeException(
                ctx,
                String.format("formal arg '%s' already defined in signature or as a constant", name)
            );
        }
    }

    private List<String> parseStringArrayLit(List<StringLitContext> ctxs) {
        if (ctxs == null) {
            return new ArrayList<>();
        }

        List<String> strings = new ArrayList<>();
        for (StringLitContext stringLitContext : ctxs) {
            strings.add(ExpressionVisitor.removeQuotes(stringLitContext));
        }

        return strings;
    }
}
