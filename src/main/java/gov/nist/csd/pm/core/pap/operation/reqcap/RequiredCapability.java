package gov.nist.csd.pm.core.pap.operation.reqcap;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * RequiredCapability maps an operation formal parameter to the access rights required to satisfy this capability.
 */
public class RequiredCapability implements Serializable {

    private final List<RequiredPrivilege> requiredPrivileges;

    public RequiredCapability(List<RequiredPrivilege> requiredPrivileges) {
        this.requiredPrivileges = requiredPrivileges;
    }

    public RequiredCapability(RequiredPrivilege requiredPrivilege, RequiredPrivilege ... requiredPrivileges) {
        this.requiredPrivileges = new ArrayList<>();
        this.requiredPrivileges.add(requiredPrivilege);
        this.requiredPrivileges.addAll(List.of(requiredPrivileges));
    }

    public List<RequiredPrivilege> getRequiredPrivileges() {
        return requiredPrivileges;
    }

    /**
     * Checks if the given user and args satisfies this RequiredCapability.
     * @param pap the PAP object used to access the policy information.
     * @param userCtx the user performing the operation.
     * @param args the args passed to the operation.
     * @return true if this RequiredCapability is satisfied.
     * @throws PMException if there is an error checking if the user has the required privileges.
     */
    public boolean isSatisfied(PAP pap, UserContext userCtx, Args args) throws PMException {
        if (requiredPrivileges.isEmpty()) {
            return true;
        }

        for (RequiredPrivilege requiredPrivilege : requiredPrivileges) {
            if (!requiredPrivilege.isSatisfied(pap, userCtx, args)) {
                return false;
            }
        }

        return true;
    }
}
