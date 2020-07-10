package gov.nist.csd.pm.pdp.services;

import gov.nist.csd.pm.epp.EPP;
import gov.nist.csd.pm.operations.OperationSet;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pdp.decider.Decider;
import gov.nist.csd.pm.pdp.decider.PReviewDecider;
import gov.nist.csd.pm.pap.policies.SuperPolicy;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.obligations.Obligations;
import gov.nist.csd.pm.pip.prohibitions.Prohibitions;

/**
 * Class to provide common methods to all services.
 */
public class Service {

    private PAP pap;
    private EPP epp;
    SuperPolicy superPolicy;
    UserContext userCtx;
    private OperationSet resourceOps;

    /**
     * Create a new Service with a sessionID and processID from the request context.
     * @param pap the Policy Administration Point
     * @param epp the Event Processing Point
     */
    Service(PAP pap, EPP epp, OperationSet resourceOps) {
        this.pap = pap;
        this.epp = epp;
        this.resourceOps = resourceOps;
    }

    private Service() {}

    public void setUserCtx(UserContext userCtx) {
        this.userCtx = userCtx;
    }

    public UserContext getUserCtx() {
        return userCtx;
    }

    EPP getEPP() {
        return this.epp;
    }

    PAP getPAP() {
        return this.pap;
    }

    Graph getGraphAdmin() {
        return pap.getGraphAdmin();
    }

    Prohibitions getProhibitionsAdmin() {
        return pap.getProhibitionsAdmin();
    }

    Obligations getObligationsAdmin() {
        return pap.getObligationsAdmin();
    }

    public OperationSet getResourceOps() {
        return resourceOps;
    }

    public void setResourceOps(OperationSet resourceOps) {
        this.resourceOps = resourceOps;
    }

    public Decider getDecider() {
        return new PReviewDecider(getGraphAdmin(), getProhibitionsAdmin(), resourceOps);
    }
}
