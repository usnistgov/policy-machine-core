package gov.nist.csd.pm.policy.pml.statement;

import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.pml.expression.Expression;
import gov.nist.csd.pm.policy.pml.function.FormalArgument;
import gov.nist.csd.pm.policy.pml.context.ExecutionContext;
import gov.nist.csd.pm.policy.pml.exception.PMLExecutionException;
import gov.nist.csd.pm.policy.pml.value.Value;
import org.antlr.v4.runtime.ParserRuleContext;

import java.io.Serializable;
import java.util.List;

public abstract class PMLStatement implements Serializable {

    private ParserRuleContext ctx;

    public PMLStatement() {}

    public PMLStatement(ParserRuleContext ctx) {
        this.ctx = ctx;
    }

    public ParserRuleContext getCtx() {
        return ctx;
    }

    public boolean hasError() {
        return ctx != null;
    }

    public abstract Value execute(ExecutionContext ctx, Policy policy) throws PMException;

    @Override
    public abstract boolean equals(Object o);

    @Override
    public abstract int hashCode();

    @Override
    public final String toString() {
        return toFormattedString(0);
    }

    public abstract String toFormattedString(int indentLevel);

    public static String indent(int indentLevel) {
        String INDENT = "    ";
        return INDENT.repeat(indentLevel);
    }
}
