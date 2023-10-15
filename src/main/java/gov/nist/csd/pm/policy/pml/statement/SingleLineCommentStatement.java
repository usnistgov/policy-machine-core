package gov.nist.csd.pm.policy.pml.statement;

import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.pml.model.context.ExecutionContext;

import gov.nist.csd.pm.policy.pml.value.Value;
import gov.nist.csd.pm.policy.pml.value.VoidValue;

import java.util.Objects;


public class SingleLineCommentStatement extends PMLStatement{

    private final String comment;

    public SingleLineCommentStatement(String comment) {
        if (comment.contains("\n")) {
            throw new IllegalArgumentException("cannot have a new line in a single comment statement");
        }
        this.comment = comment;
    }

    @Override
    public Value execute(ExecutionContext ctx, Policy policy) throws PMException {
        return new VoidValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SingleLineCommentStatement that = (SingleLineCommentStatement) o;
        return Objects.equals(comment, that.comment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(comment);
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return indent(indentLevel) + "// " + comment;
    }
}
