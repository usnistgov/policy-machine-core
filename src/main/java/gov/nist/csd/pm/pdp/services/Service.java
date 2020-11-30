package gov.nist.csd.pm.pdp.services;

import gov.nist.csd.pm.epp.EPP;
import gov.nist.csd.pm.operations.OperationSet;
import gov.nist.csd.pm.pdp.audit.Auditor;
import gov.nist.csd.pm.pdp.decider.Decider;
import gov.nist.csd.pm.pdp.decider.PReviewDecider;
import gov.nist.csd.pm.pap.policies.SuperPolicy;
import gov.nist.csd.pm.pip.Features;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.obligations.Obligations;
import gov.nist.csd.pm.pip.prohibitions.Prohibitions;

/**
 * Class to provide common methods to all services.
 */
public class Service {

    private Features pap;
    private EPP epp;
    SuperPolicy superPolicy;
    UserContext userCtx;
    private Decider decider;
    private Auditor auditor;

    /**
     * Create a new Service with a sessionID and processID from the request context.
     * @param pap the Policy Administration Point
     * @param epp the Event Processing Point
     */
    Service(UserContext userCtx, Features pap, EPP epp, Decider decider, Auditor auditor) {
        this.userCtx = userCtx;
        this.pap = pap;
        this.epp = epp;
        this.decider = decider;
        this.auditor = auditor;
    }

    private Service() {}

    public UserContext getUserCtx() {
        return userCtx;
    }

    EPP getEPP() {
        return this.epp;
    }

    Features getPAP() {
        return this.pap;
    }

    Graph getGraphAdmin() {
        return pap.getGraph();
    }

    Prohibitions getProhibitionsAdmin() {
        return pap.getProhibitions();
    }

    Obligations getObligationsAdmin() {
        return pap.getObligations();
    }

    public Decider getDecider() {
        return decider;
    }

    public Auditor getAuditor() {
        return auditor;
    }

}
