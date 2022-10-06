package gov.nist.csd.pm.policy.author.pal.compiler.visitor;

import gov.nist.csd.pm.policy.author.pal.antlr.PALBaseVisitor;
import gov.nist.csd.pm.policy.author.pal.antlr.PALParser;
import gov.nist.csd.pm.policy.author.pal.model.expression.ArrayLiteral;
import gov.nist.csd.pm.policy.author.pal.model.expression.Literal;
import gov.nist.csd.pm.policy.author.pal.model.expression.Type;
import gov.nist.csd.pm.policy.author.pal.model.expression.VariableReference;
import gov.nist.csd.pm.policy.author.pal.statement.Expression;
import gov.nist.csd.pm.policy.author.pal.statement.NameExpression;

import java.util.ArrayList;
import java.util.List;

public class AccessRightsVisitor extends PALBaseVisitor<NameExpression> {

    @Override
    public NameExpression visitAccessRightArray(PALParser.AccessRightArrayContext ctx) {
        List<NameExpression> exprs = new ArrayList<>();
        for (PALParser.AccessRightContext accessRightCtx : ctx.accessRight()) {
            exprs.add(new NameExpression(new VariableReference(accessRightCtx.getText(), Type.string())));
        }
        return new NameExpression(exprs);
    }

}
