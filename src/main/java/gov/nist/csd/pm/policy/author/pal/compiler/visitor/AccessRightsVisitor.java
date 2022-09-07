package gov.nist.csd.pm.policy.author.pal.compiler.visitor;

import gov.nist.csd.pm.policy.author.pal.antlr.PALBaseVisitor;
import gov.nist.csd.pm.policy.author.pal.antlr.PALParser;
import gov.nist.csd.pm.policy.author.pal.model.expression.ArrayLiteral;
import gov.nist.csd.pm.policy.author.pal.model.expression.Literal;
import gov.nist.csd.pm.policy.author.pal.model.expression.Type;
import gov.nist.csd.pm.policy.author.pal.statement.Expression;

import java.util.ArrayList;
import java.util.List;

public class AccessRightsVisitor extends PALBaseVisitor<Expression> {

    @Override
    public Expression visitAccessRightArray(PALParser.AccessRightArrayContext ctx) {
        List<Expression> exprs = new ArrayList<>();
        for (PALParser.AccessRightContext accessRightCtx : ctx.accessRight()) {
            exprs.add(new Expression(new Literal(accessRightCtx.getText())));
        }
        return new Expression(new Literal(new ArrayLiteral(exprs.toArray(Expression[]::new), Type.string())));
    }

}
