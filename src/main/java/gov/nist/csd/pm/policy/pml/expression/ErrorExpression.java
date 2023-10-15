package gov.nist.csd.pm.policy.pml.expression;

import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.pml.model.context.ExecutionContext;
import gov.nist.csd.pm.policy.pml.model.scope.PMLScopeException;
import gov.nist.csd.pm.policy.pml.model.scope.Scope;
import gov.nist.csd.pm.policy.pml.type.Type;
import gov.nist.csd.pm.policy.pml.value.StringValue;
import gov.nist.csd.pm.policy.pml.value.Value;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.Objects;

public class ErrorExpression extends Expression {

    private final ParserRuleContext ctx;

    public ErrorExpression(ParserRuleContext ctx) {
        this.ctx = ctx;
    }
    @Override
    public Type getType(Scope scope) throws PMLScopeException {
        return Type.any();
    }

    @Override
    public Value execute(ExecutionContext ctx, Policy policy) throws PMException {
        return new StringValue(this.ctx.getText());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ErrorExpression that = (ErrorExpression) o;
        return Objects.equals(ctx, that.ctx);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.ctx.getText());
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return ctx.getText();
    }

}
