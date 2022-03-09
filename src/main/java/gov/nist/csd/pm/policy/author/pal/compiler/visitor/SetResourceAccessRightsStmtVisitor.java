package gov.nist.csd.pm.policy.author.pal.compiler.visitor;

import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.author.pal.antlr.PALBaseVisitor;
import gov.nist.csd.pm.policy.author.pal.antlr.PALParser;
import gov.nist.csd.pm.policy.author.pal.model.context.VisitorContext;
import gov.nist.csd.pm.policy.author.pal.model.expression.Type;
import gov.nist.csd.pm.policy.author.pal.statement.SetResourceAccessRightsStatement;

import java.util.List;

public class SetResourceAccessRightsStmtVisitor extends PALBaseVisitor<SetResourceAccessRightsStatement> {

    private final VisitorContext visitorCtx;

    public SetResourceAccessRightsStmtVisitor(VisitorContext visitorCtx) {
        this.visitorCtx = visitorCtx;
    }

    @Override
    public SetResourceAccessRightsStatement visitSetResourceAccessRightsStmt(PALParser.SetResourceAccessRightsStmtContext ctx) {
        PALParser.AccessRightArrayContext accessRightArrayCtx = ctx.accessRightArray();
        List<PALParser.AccessRightContext> identifiers = accessRightArrayCtx.accessRight();
        AccessRightSet arset = new AccessRightSet();
        for (PALParser.AccessRightContext id : identifiers) {
            String ar = id.getText();
            arset.add(ar);

            this.visitorCtx.scope().addVariable(ar, Type.string(), true);
        }

        return new SetResourceAccessRightsStatement(arset);
    }
}
