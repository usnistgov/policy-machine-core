package gov.nist.csd.pm.pdp.services;

import gov.nist.csd.pm.epp.EPP;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pdp.audit.Auditor;
import gov.nist.csd.pm.pdp.decider.Decider;
import gov.nist.csd.pm.pap.policies.SuperPolicy;
import gov.nist.csd.pm.common.FunctionalEntity;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.obligations.Obligations;
import gov.nist.csd.pm.pip.prohibitions.Prohibitions;

/**
 * Class to provide common methods to all services.
 */
public class Service {

    private FunctionalEntity pap;
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
    Service(UserContext userCtx, FunctionalEntity pap, EPP epp, Decider decider, Auditor auditor) {
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

    public void setUserCtx(UserContext userCtx) {
        this.userCtx = userCtx;
    }

    EPP getEPP() {
        return this.epp;
    }

    FunctionalEntity getPAP() {
        return this.pap;
    }

    Graph getGraphAdmin() throws PMException {
        return pap.getGraph();
    }

    Prohibitions getProhibitionsAdmin() throws PMException {
        return pap.getProhibitions();
    }

    Obligations getObligationsAdmin() throws PMException {
        return pap.getObligations();

    }

    public Decider getDecider() {
        return decider;
    }

    public Auditor getAuditor() {
        return auditor;
    }

}
