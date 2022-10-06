package gov.nist.csd.pm.policy.author.pal.statement;

import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.author.pal.model.context.ExecutionContext;
import gov.nist.csd.pm.policy.author.pal.model.expression.Value;
import gov.nist.csd.pm.policy.author.PolicyAuthor;

public class SetResourceAccessRightsStatement extends PALStatement{

    private final AccessRightSet accessRightSet;

    public SetResourceAccessRightsStatement(AccessRightSet accessRightSet) {
        this.accessRightSet = accessRightSet;
    }

    @Override
    public Value execute(ExecutionContext ctx, PolicyAuthor policyAuthor) throws PMException {
        policyAuthor.graph().setResourceAccessRights(accessRightSet);
        ctx.scope().setResourceAccessRights(accessRightSet);
        return new Value();
    }

    @Override
    public String toString() {
        StringBuilder ars = new StringBuilder();
        for (String ar : accessRightSet) {
            if (ars.length() > 0) {
                ars.append(", ");
            }

            ars.append(String.format("%s", ar));
        }
        return String.format("set resource access rights %s;", ars);
    }
}
