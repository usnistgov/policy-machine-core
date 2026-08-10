package gov.nist.ngac.pm.core.pap.pml.statement;

import java.io.Serializable;

/**
 * Anything that can render itself back to PML source text, at a given indent level.
 */
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
