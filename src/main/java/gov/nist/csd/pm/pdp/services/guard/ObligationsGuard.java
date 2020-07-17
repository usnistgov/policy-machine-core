package gov.nist.csd.pm.pdp.services.guard;

import gov.nist.csd.pm.exceptions.PMAuthorizationException;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.operations.OperationSet;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pdp.services.UserContext;

import static gov.nist.csd.pm.operations.Operations.*;

public class ObligationsGuard extends Guard {

    public ObligationsGuard(PAP pap, OperationSet resourceOps) {
        super(pap, resourceOps);
    }

    public void checkAdd(UserContext userCtx) throws PMException {
        // check that the user can create a policy class
        if (!hasPermissions(userCtx, pap.getGraphAdmin().getSuperPolicy().getSuperObjectAttribute().getName(), ADD_OBLIGATION)) {
            throw new PMAuthorizationException("unauthorized permissions to create a policy class");
        }
    }

    public void checkGet(UserContext userCtx) throws PMException {
        // check that the user can create a policy class
        if (!hasPermissions(userCtx, pap.getGraphAdmin().getSuperPolicy().getSuperObjectAttribute().getName(), GET_OBLIGATION)) {
            throw new PMAuthorizationException("unauthorized permissions to create a policy class");
        }
    }

    public void checkUpdate(UserContext userCtx) throws PMException {
        // check that the user can create a policy class
        if (!hasPermissions(userCtx, pap.getGraphAdmin().getSuperPolicy().getSuperObjectAttribute().getName(), UPDATE_OBLIGATION)) {
            throw new PMAuthorizationException("unauthorized permissions to create a policy class");
        }
    }

    public void checkDelete(UserContext userCtx) throws PMException {
        // check that the user can create a policy class
        if (!hasPermissions(userCtx, pap.getGraphAdmin().getSuperPolicy().getSuperObjectAttribute().getName(), DELETE_OBLIGATION)) {
            throw new PMAuthorizationException("unauthorized permissions to create a policy class");
        }
    }

    public void checkEnable(UserContext userCtx) throws PMException {
        // check that the user can create a policy class
        if (!hasPermissions(userCtx, pap.getGraphAdmin().getSuperPolicy().getSuperObjectAttribute().getName(), ENABLE_OBLIGATION)) {
            throw new PMAuthorizationException("unauthorized permissions to create a policy class");
        }
    }
}
