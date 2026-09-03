/*
 * This Software (Policy Machine) is being made available as a public service by the
 * National Institute of Standards and Technology (NIST), an Agency of the United
 * States Department of Commerce. This software was developed in part by employees of
 * NIST and in part by NIST contractors. Copyright in portions of this software that
 * were developed by NIST contractors has been licensed or assigned to NIST. Pursuant
 * to Title 17 United States Code Section 105, works of NIST employees are not
 * subject to copyright protection in the United States. However, NIST may hold
 * international copyright in software created by its employees and domestic
 * copyright (or licensing rights) in portions of software that were assigned or
 * licensed to NIST. To the extent that NIST holds copyright in this software, it is
 * being made available under the Creative Commons Attribution 4.0 International
 * license (CC BY 4.0). The disclaimers of the CC BY 4.0 license apply to all parts
 * of the software developed or licensed by NIST.
 *
 * ACCESS THE FULL CC BY 4.0 LICENSE HERE:
 * https://creativecommons.org/licenses/by/4.0/legalcode
 */

package gov.nist.ngac.pm.core.pap.operation.reqcap;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.PAP;
import gov.nist.ngac.pm.core.pap.operation.arg.Args;
import gov.nist.ngac.pm.core.pap.query.model.context.UserContext;
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
