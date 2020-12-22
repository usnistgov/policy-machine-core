package gov.nist.csd.pm.pdp.services.guard;

import gov.nist.csd.pm.exceptions.PMAuthorizationException;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pdp.decider.Decider;
import gov.nist.csd.pm.pdp.services.UserContext;
import gov.nist.csd.pm.common.FunctionalEntity;
import gov.nist.csd.pm.pip.prohibitions.model.Prohibition;

import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.operations.Operations.*;

public class ProhibitionsGuard extends Guard {

    public ProhibitionsGuard(FunctionalEntity pap, Decider decider) {
        super(pap, decider);
    }

    private void check(UserContext userCtx, Prohibition prohibition, String permission) throws PMException {
        String subject = prohibition.getSubject();
        Map<String, Boolean> containers = prohibition.getContainers();

        // check prohibition subject
        if (pap.getGraph().exists(subject)) {
            if (!hasPermissions(userCtx, subject, permission)) {
                throw new PMAuthorizationException(String.format("unauthorized permission %s on %s", permission, subject));
            }
        }

        // check each container in prohibition
        for (String container : containers.keySet()) {
            if (!hasPermissions(userCtx, container, permission)) {
                throw new PMAuthorizationException(String.format("unauthorized permission %s on %s", permission, container));
            }
        }
    }

    public void checkAdd(UserContext userCtx, Prohibition prohibition) throws PMException {
        check(userCtx, prohibition, CREATE_PROHIBITION);
    }

    public void checkGet(UserContext userCtx, Prohibition prohibition) throws PMException {
        check(userCtx, prohibition, VIEW_PROHIBITION);
    }

    public void checkUpdate(UserContext userCtx, Prohibition prohibition) throws PMException {
        check(userCtx, prohibition, UPDATE_PROHIBITION);
    }

    public void checkDelete(UserContext userCtx, Prohibition prohibition) throws PMException {
        check(userCtx, prohibition, DELETE_PROHIBITION);
    }

    public void filter(UserContext userCtx, List<Prohibition> prohibitions) {
        prohibitions.removeIf(prohibition -> {
            try {
                checkGet(userCtx, prohibition);
                return false;
            } catch (PMException e) {
                return true;
            }
        });
    }
}
