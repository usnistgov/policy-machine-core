package gov.nist.csd.pm.core.pap.function;

import gov.nist.csd.pm.core.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.core.pap.query.model.context.TargetContext;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.pdp.UnauthorizedException;

/**
 * RequiredCapabilities represents the access rights needed on an argument of an operation.
 */
public class RequiredCapabilities {

    private AccessRightSet reqCaps;

    public RequiredCapabilities(AccessRightSet reqCaps) {
        this.reqCaps = reqCaps;
    }

    public RequiredCapabilities(String ... reqCaps) {
        this.reqCaps = new AccessRightSet(reqCaps);
    }

    public AccessRightSet getReqCaps() {
        return reqCaps;
    }

    public void setReqCaps(AccessRightSet reqCaps) {
        this.reqCaps = reqCaps;
    }

    /**
     * Checks if the provided user privileges satisfies the required capabilities.
     * @param user The user context.
     * @param target The target context.
     * @param userPrivileges The privileges a user has, to be checked against the required capabilities.
     * @throws UnauthorizedException if the provided privileges do not satisfy the required capabilities.
     */
    public void check(UserContext user, TargetContext target, AccessRightSet userPrivileges) throws UnauthorizedException {
        if(userPrivileges.containsAll(reqCaps)) {
            return;
        }

        AccessRightSet copy = new AccessRightSet(reqCaps);
        copy.removeAll(userPrivileges);
        throw new UnauthorizedException(user, target, copy);
    }

}
