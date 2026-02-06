package gov.nist.csd.pm.core.pap.pml.compiler.visitor.operation;

import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.VOID_TYPE;

import gov.nist.csd.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.csd.pm.core.pap.operation.param.FormalParameter;
import gov.nist.csd.pm.core.pap.operation.param.NodeFormalParameter;
import gov.nist.csd.pm.core.pap.operation.reqcap.RequiredCapability;
import gov.nist.csd.pm.core.pap.operation.reqcap.RequiredCapabilityFunc;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser.ArsetEntryContext;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser.ArsetReqCapContext;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser.FuncReqCapContext;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser.ReqCapContext;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser.ReqCapListContext;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser.StringArrayLitContext;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser.StringLitContext;
import gov.nist.csd.pm.core.pap.pml.compiler.visitor.ExpressionVisitor;
import gov.nist.csd.pm.core.pap.pml.compiler.visitor.PMLBaseVisitor;
import gov.nist.csd.pm.core.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.core.pap.pml.exception.PMLCompilationRuntimeException;
import gov.nist.csd.pm.core.pap.pml.operation.PMLRequiredCapabilityFunc;
import gov.nist.csd.pm.core.pap.pml.statement.PMLStatementBlock;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReqCapListVisitor extends PMLBaseVisitor<List<RequiredCapability>> {

    private List<FormalParameter<?>> args;

    public ReqCapListVisitor(VisitorContext visitorCtx, List<FormalParameter<?>> args) {
        super(visitorCtx);
        this.args = args;
    }

    @Override
    public List<RequiredCapability> visitReqCapList(ReqCapListContext ctx) {
        List<RequiredCapability> reqCapList = new ArrayList<>();

        if (ctx == null) {
            return reqCapList;
        }

        ReqCapVisitor reqCapVisitor = new ReqCapVisitor(visitorCtx, args);
        for (ReqCapContext reqCapContext : ctx.reqCap()) {
            reqCapList.add(reqCapVisitor.visitReqCap(reqCapContext));
        }

        return reqCapList;
    }

    static class ReqCapVisitor extends PMLBaseVisitor<RequiredCapability> {

        private List<FormalParameter<?>> args;

        public ReqCapVisitor(VisitorContext visitorCtx, List<FormalParameter<?>> args) {
            super(visitorCtx);
            this.args = args;
        }

        @Override
        public RequiredCapability visitReqCap(ReqCapContext ctx) {
            if (ctx.arsetReqCap() != null) {
                return visitArsetReqCap(ctx.arsetReqCap());
            } else {
                return visitFuncReqCap(ctx.funcReqCap());
            }
        }

        @Override
        public RequiredCapability visitArsetReqCap(ArsetReqCapContext ctx) {
            List<ArsetEntryContext> arsetEntryContexts = ctx.arsetEntry();
            Map<NodeFormalParameter<?>, AccessRightSet> arMap = new HashMap<>();
            for (ArsetEntryContext arsetEntryContext : arsetEntryContexts) {
                String param = arsetEntryContext.ID().getText();
                NodeFormalParameter<?> formalParameter = findNodeFormalParameter(param);

                List<String> ars = parseStringArrayLit(arsetEntryContext.stringArrayLit());

                // check param in scope
                arMap.put(formalParameter, new AccessRightSet(ars));
            }

            return new RequiredCapability(arMap);
        }

        @Override
        public RequiredCapability visitFuncReqCap(FuncReqCapContext ctx) {
            PMLStatementBlock pmlStatementBlock = StatementBlockParser.parseBasicOrCheckStatements(visitorCtx,
                ctx.basicAndCheckStatementBlock(), VOID_TYPE, args);
            return new PMLRequiredCapabilityFunc(pmlStatementBlock);
        }

        private NodeFormalParameter<?> findNodeFormalParameter(String name) {
            for (FormalParameter<?> arg : args) {
                if (!arg.getName().equals(name)) {
                    continue;
                }

                if (!(arg instanceof NodeFormalParameter<?> nodeFormalParameter)) {
                    throw new PMLCompilationRuntimeException(name + " must be annotated with @node to be used in the reqcap");
                }

                return nodeFormalParameter;
            }

            throw new PMLCompilationRuntimeException("unknown parameter in reqcap " + name);
        }

        private List<String> parseStringArrayLit(StringArrayLitContext ctx) {
            List<String> strings = new ArrayList<>();
            for (StringLitContext litCtx : ctx.stringLit()) {
                strings.add(ExpressionVisitor.removeQuotes(litCtx));
            }

            return strings;
        }
    }
}
