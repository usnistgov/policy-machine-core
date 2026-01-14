package gov.nist.csd.pm.core.pap.pml.compiler.visitor.function;

import static gov.nist.csd.pm.core.pap.function.arg.type.BasicTypes.STRING_TYPE;

import gov.nist.csd.pm.core.pap.function.RequiredCapabilities;
import gov.nist.csd.pm.core.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.core.pap.function.arg.type.ListType;
import gov.nist.csd.pm.core.pap.function.arg.type.NodeType;
import gov.nist.csd.pm.core.pap.function.arg.type.Type;
import gov.nist.csd.pm.core.pap.function.op.arg.NodeFormalParameter;
import gov.nist.csd.pm.core.pap.function.op.arg.NodeListFormalParameter;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser.FormalParamContext;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser.OperationFormalParamContext;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser.OperationFormalParamListContext;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser.StringArrayLitContext;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser.StringLitContext;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser.VariableTypeContext;
import gov.nist.csd.pm.core.pap.pml.compiler.visitor.ExpressionVisitor;
import gov.nist.csd.pm.core.pap.pml.compiler.visitor.PMLBaseVisitor;
import gov.nist.csd.pm.core.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.core.pap.pml.exception.PMLCompilationRuntimeException;
import gov.nist.csd.pm.core.pap.pml.expression.Expression;
import gov.nist.csd.pm.core.pap.pml.expression.literal.ArrayLiteralExpression;
import gov.nist.csd.pm.core.pap.pml.type.TypeResolver;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.TerminalNode;

public class FormalParameterListVisitor extends PMLBaseVisitor<List<FormalParameter<?>>> {

    public FormalParameterListVisitor(VisitorContext visitorCtx) {
        super(visitorCtx);
    }

    @Override
    public List<FormalParameter<?>> visitOperationFormalParamList(OperationFormalParamListContext ctx) {
        return parseParamList(
            ctx.operationFormalParam(),
            OperationFormalParamContext::ID,
            OperationFormalParamContext::variableType,
            true,
            c -> parseStringArrayLit(c.reqCap)
        );
    }

    @Override
    public List<FormalParameter<?>> visitFormalParamList(PMLParser.FormalParamListContext ctx) {
        return parseParamList(
            ctx.formalParam(),
            FormalParamContext::ID,
            FormalParamContext::variableType,
            false,
            c -> new ArrayList<>()
        );
    }

    private <T extends ParserRuleContext> List<FormalParameter<?>> parseParamList(
        List<T> paramCtxs,
        Function<T, TerminalNode> ifFunc,
        Function<T, VariableTypeContext> typeFunc,
        boolean isOperation,
        Function<T, List<String>> reqCapsFunc
    ) {
        List<FormalParameter<?>> params = new ArrayList<>();
        Set<String> paramNames = new HashSet<>();

        for (T paramCtx : paramCtxs) {
            String name = ifFunc.apply(paramCtx).getText();

            if (!paramNames.add(name)) {
                throw new PMLCompilationRuntimeException(
                    paramCtx,
                    String.format("formal arg '%s' already defined in signature", name)
                );
            }

            Type<?> type = TypeResolver.resolveFromParserCtx(typeFunc.apply(paramCtx));

            if (isOperation) {
                RequiredCapabilities reqCap = new RequiredCapabilities(reqCapsFunc.apply(paramCtx));

                if (type instanceof NodeType) {
                    params.add(new NodeFormalParameter(name, reqCap));
                    continue;
                } else if (type instanceof ListType<?> listType && listType.getElementType() instanceof NodeType) {
                    params.add(new NodeListFormalParameter(name, reqCap));
                    continue;
                }
            }

            params.add(new FormalParameter<>(name, type));
        }

        return params;
    }

    private List<String> parseStringArrayLit(StringArrayLitContext ctx) {
        if (ctx == null) {
            return new ArrayList<>();
        }

        List<StringLitContext> stringLits = ctx.stringLit();
        List<String> strings = new ArrayList<>();
        for (StringLitContext stringLitContext : stringLits) {
            strings.add(ExpressionVisitor.removeQuotes(stringLitContext.getText()));
        }

        return strings;
    }
}
