package gov.nist.csd.pm.core.pap.pml.compiler.visitor.operation;

import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.VOID_TYPE;

import gov.nist.csd.pm.core.pap.operation.param.FormalParameter;
import gov.nist.csd.pm.core.pap.operation.reqcap.RequiredCapability;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser.ReqCapContext;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser.ReqCapListContext;
import gov.nist.csd.pm.core.pap.pml.compiler.visitor.PMLBaseVisitor;
import gov.nist.csd.pm.core.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.core.pap.pml.operation.PMLRequiredCapabilityFunc;
import gov.nist.csd.pm.core.pap.pml.statement.PMLStatementBlock;
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

        for (ReqCapContext reqCapContext : ctx.reqCap()) {
            PMLStatementBlock pmlStatementBlock = StatementBlockParser.parseBasicStatementBlock(
                visitorCtx,
                reqCapContext.basicStatementBlock(),
                VOID_TYPE,
                args,
                true
            );

            PMLRequiredCapabilityFunc pmlRequiredCapabilityFunc = new PMLRequiredCapabilityFunc(pmlStatementBlock);

            reqCapList.add(pmlRequiredCapabilityFunc);
        }

        return reqCapList;
    }
}
