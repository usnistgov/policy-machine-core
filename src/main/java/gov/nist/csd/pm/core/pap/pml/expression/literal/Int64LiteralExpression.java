package gov.nist.csd.pm.core.pap.pml.expression.literal;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.function.arg.type.BasicTypes;
import gov.nist.csd.pm.core.pap.function.arg.type.Type;
import gov.nist.csd.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.core.pap.pml.expression.Expression;
import java.util.Objects;

public class Int64LiteralExpression  extends Expression<Long> {

    long value;

    public Int64LiteralExpression(long value) {
        this.value = value;
    }

    public long getValue() {
        return value;
    }

    @Override
    public Type<Long> getType() {
        return BasicTypes.LONG_TYPE;
    }

    @Override
    public Long execute(ExecutionContext ctx, PAP pap) throws PMException {
        return value;
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return indent(indentLevel) + value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Int64LiteralExpression that)) {
            return false;
        }
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
