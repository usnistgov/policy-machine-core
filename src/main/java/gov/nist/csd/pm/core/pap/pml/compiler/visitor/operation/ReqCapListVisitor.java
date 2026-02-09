package gov.nist.csd.pm.core.pap.pml.compiler.visitor.operation;

import gov.nist.csd.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.csd.pm.core.pap.operation.param.FormalParameter;
import gov.nist.csd.pm.core.pap.operation.param.NodeFormalParameter;
import gov.nist.csd.pm.core.pap.operation.reqcap.RequiredCapability;
import gov.nist.csd.pm.core.pap.operation.reqcap.RequiredPrivilege;
import gov.nist.csd.pm.core.pap.operation.reqcap.RequiredPrivilegeOnNodeName;
import gov.nist.csd.pm.core.pap.operation.reqcap.RequiredPrivilegeOnParameter;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser.ReqCapContext;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser.ReqCapEntryContext;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser.ReqCapListContext;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser.StringArrayLitContext;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser.StringLitContext;
import gov.nist.csd.pm.core.pap.pml.compiler.visitor.ExpressionVisitor;
import gov.nist.csd.pm.core.pap.pml.compiler.visitor.PMLBaseVisitor;
import gov.nist.csd.pm.core.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.core.pap.pml.exception.PMLCompilationRuntimeException;
import java.util.ArrayList;
import java.util.List;

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
            List<RequiredPrivilege> requiredPrivileges = new ArrayList<>();

            for (ReqCapEntryContext entryCtx : ctx.reqCapEntry()) {
                AccessRightSet arset = parseArsetExpression(entryCtx.arset);

                if (entryCtx.param != null) {
                    String paramName = entryCtx.param.getText();
                    NodeFormalParameter<?> formalParameter = findNodeFormalParameter(paramName);
                    requiredPrivileges.add(new RequiredPrivilegeOnParameter(formalParameter, arset));
                } else {
                    requiredPrivileges.add(new RequiredPrivilegeOnNodeName(ExpressionVisitor.removeQuotes(entryCtx.node), arset));
                }
            }

            return new RequiredCapability(requiredPrivileges);
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

        private AccessRightSet parseArsetExpression(StringArrayLitContext arsetCtx) {
            AccessRightSet arset = new AccessRightSet();
            for (StringLitContext stringLitContext : arsetCtx.stringLit()) {
                arset.add(ExpressionVisitor.removeQuotes(stringLitContext));
            }

            return arset;
        }

        private List<String> parseArrayLiteral(PMLParser.ArrayLitContext arrayLitCtx) {
            List<String> strings = new ArrayList<>();
            if (arrayLitCtx.expressionList() == null) {
                return strings;
            }

            for (PMLParser.ExpressionContext exprCtx : arrayLitCtx.expressionList().expression()) {
                if (exprCtx instanceof PMLParser.LiteralExpressionContext elemLitCtx) {
                    PMLParser.LiteralContext elemLit = elemLitCtx.literal();
                    if (elemLit instanceof PMLParser.StringLiteralContext strLitCtx) {
                        strings.add(ExpressionVisitor.removeQuotes(strLitCtx.stringLit()));
                        continue;
                    }
                }
                throw new PMLCompilationRuntimeException("expected string literal in reqcap access rights array");
            }

            return strings;
        }
    }
}
