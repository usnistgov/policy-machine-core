package gov.nist.csd.pm.policy.pml.statement;

import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.pml.model.context.ExecutionContext;
import gov.nist.csd.pm.policy.pml.value.Value;
import gov.nist.csd.pm.policy.pml.value.VoidValue;
import org.antlr.v4.runtime.RuleContext;

public class ErrorStatement extends PMLStatement{

    private RuleContext ctx;

    public ErrorStatement(RuleContext ctx) {
        this.ctx = ctx;
    }

    public RuleContext getCtx() {
        return ctx;
    }

    @Override
    public Value execute(ExecutionContext ctx, Policy policy) throws PMException {
        return new VoidValue();
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof ErrorStatement;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return "error at " + ctx.getText();
    }
}
