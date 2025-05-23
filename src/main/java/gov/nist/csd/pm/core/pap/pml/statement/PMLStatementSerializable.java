package gov.nist.csd.pm.core.pap.pml.statement;

import java.io.Serializable;

public interface PMLStatementSerializable extends Serializable {

    String toFormattedString(int indentLevel);

    default String indent(int indentLevel) {
        String INDENT = "    ";
        return INDENT.repeat(indentLevel);
    }

    @Override
    boolean equals(Object o);

    @Override
    int hashCode();
}
