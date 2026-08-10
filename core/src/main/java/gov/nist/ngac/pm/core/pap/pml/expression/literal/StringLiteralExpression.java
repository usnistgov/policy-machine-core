package gov.nist.ngac.pm.core.pap.pml.expression.literal;

import static gov.nist.ngac.pm.core.pap.operation.arg.type.BasicTypes.STRING_TYPE;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.PAP;
import gov.nist.ngac.pm.core.pap.operation.arg.type.StringType;
import gov.nist.ngac.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.ngac.pm.core.pap.pml.expression.Expression;
import java.util.Objects;

/**
 * PML string literal expression.
 */
public class StringLiteralExpression extends Expression<String> {

    private String value;

    public StringLiteralExpression(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public StringType getType() {
        return STRING_TYPE;
    }

    @Override
    public String execute(ExecutionContext ctx, PAP pap) throws PMException {
        return value;
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return indent(indentLevel) + String.format("\"%s\"", value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StringLiteralExpression that)) {
            return false;
        }
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
