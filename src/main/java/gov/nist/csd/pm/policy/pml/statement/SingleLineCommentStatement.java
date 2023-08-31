package gov.nist.csd.pm.policy.pml.statement;

import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.pml.model.context.ExecutionContext;
import gov.nist.csd.pm.policy.pml.model.expression.Value;

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
        return new Value();
    }

    @Override
    public String toString() {
        return "# " + comment;
    }
}
