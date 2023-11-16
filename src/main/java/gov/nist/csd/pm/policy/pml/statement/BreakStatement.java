package gov.nist.csd.pm.policy.pml.statement;

import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.pml.context.ExecutionContext;
import gov.nist.csd.pm.policy.pml.value.BreakValue;
import gov.nist.csd.pm.policy.pml.value.Value;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.Objects;


public class BreakStatement extends PMLStatement {

    public BreakStatement() {
    }

    public BreakStatement(ParserRuleContext ctx) {
        super(ctx);
    }

    @Override
    public Value execute(ExecutionContext ctx, Policy policy) throws PMException {
        return new BreakValue();
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof BreakStatement;
    }

    @Override
    public int hashCode() {
        return Objects.hash(toString());
    }


    @Override
    public String toFormattedString(int indentLevel) {
        return indent(indentLevel) + "break";
    }
}
