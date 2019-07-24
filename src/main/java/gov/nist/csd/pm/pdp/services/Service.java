package gov.nist.csd.pm.pdp.services;

import gov.nist.csd.pm.epp.EPP;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pdp.decider.Decider;
import gov.nist.csd.pm.pdp.decider.MemProhibitionDecider;
import gov.nist.csd.pm.pdp.decider.PReviewDecider;
import gov.nist.csd.pm.pdp.decider.ProhibitionDecider;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.obligations.Obligations;
import gov.nist.csd.pm.pip.prohibitions.Prohibitions;

import java.util.Arrays;
import java.util.Set;

import static gov.nist.csd.pm.pdp.decider.PReviewDecider.ANY_OPERATIONS;

/**
 * Class to provide common methods to all services.
 */
public class Service {

    private PAP pap;
    private EPP epp;

    /**
     * Create a new Service with a sessionID and processID from the request context.
     * @param pap the Policy Administration Point
     * @param epp the Event Processing Point
     */
    public Service(PAP pap, EPP epp) {
        this.pap = pap;
        this.epp = epp;
    }

    private Service() {}

    protected EPP getEPP() {
        return this.epp;
    }

    protected PAP getPAP() {
        return this.pap;
    }

    Graph getGraphPAP() {
        return pap.getGraphPAP();
    }

    Prohibitions getProhibitionsPAP() {
        return pap.getProhibitionsPAP();
    }

    Obligations getObligationsPAP() {
        return pap.getObligationsPAP();
    }

    public Decider getDecider() throws PMException {
        return new PReviewDecider(getGraphPAP());
    }

    protected boolean hasPermissions(UserContext userCtx, long targetID, String ... permissions) throws PMException {
        Decider decider = new PReviewDecider(pap.getGraphPAP());
        ProhibitionDecider prohibitionDecider = new MemProhibitionDecider(pap.getGraphPAP(), pap.getProhibitionsPAP().getAll());

        Set<String> perms = decider.listPermissions(userCtx.getUserID(), targetID);
        Set<String> deniedPerms = prohibitionDecider.listProhibitedPermissions(userCtx.getUserID(), targetID);
        perms.removeAll(deniedPerms);
        deniedPerms = prohibitionDecider.listProhibitedPermissions(userCtx.getProcessID(), targetID);
        perms.removeAll(deniedPerms);

        for(String p : permissions) {
            if(p.equals(ANY_OPERATIONS)) {
                return !perms.isEmpty();
            }
        }

        if (perms.contains("*")) {
            return true;
        } else {
            return !perms.isEmpty() && perms.containsAll(Arrays.asList(permissions));
        }
    }
}
