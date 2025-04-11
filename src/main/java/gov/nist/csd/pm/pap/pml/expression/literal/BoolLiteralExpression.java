package gov.nist.csd.pm.pap.pml.expression.literal;

import static gov.nist.csd.pm.pap.function.arg.type.ArgType.BOOLEAN_TYPE;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.function.arg.type.BooleanType;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.expression.Expression;
import java.util.Objects;

public class BoolLiteralExpression extends Expression<Boolean> {

    private Boolean value;

    public BoolLiteralExpression(Boolean value) {
        this.value = value;
    }

    @Override
    public BooleanType getType() {
        return BOOLEAN_TYPE;
    }

    @Override
    public Boolean execute(ExecutionContext ctx, PAP pap) throws PMException {
        return value;
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return indent(indentLevel) + value.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BoolLiteralExpression that)) {
            return false;
        }
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
