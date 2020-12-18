package gov.nist.csd.pm.pdp;

import gov.nist.csd.pm.epp.EPP;
import gov.nist.csd.pm.epp.EPPOptions;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pdp.audit.Auditor;
import gov.nist.csd.pm.pdp.decider.Decider;
import gov.nist.csd.pm.pdp.services.*;
import gov.nist.csd.pm.pip.Features;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.obligations.Obligations;
import gov.nist.csd.pm.pip.prohibitions.Prohibitions;
import gov.nist.csd.pm.pip.tx.TxRunner;

public class PDP {

    /**
     * Factory method to create a new PDP and EPP, while abstracting the initialization process. A PDP needs an EPP, and
     * vice versa. These steps need to occur each time a new PDP/EPP is created. The order of each step matters inorder to
     * avoid any race conditions. The EPP is accessible via the PDP's getEPP method.
     *
     * @return the new PDP instance
     */
    public static PDP newPDP(Features pap, EPPOptions eppOptions, Decider decider, Auditor auditor) throws PMException {
        // create PDP
        PDP pdp = new PDP(pap, decider, auditor);
        // create the EPP
        EPP epp = new EPP(pap, pdp, eppOptions);
        // set the PDPs EPP
        pdp.setEPP(epp);

        return pdp;
    }

    private final Features pap;
    private EPP epp;
    private Decider decider;
    private Auditor auditor;

    /**
     * Create a new PDP instance given a Policy Administration Point and an optional set of FunctionExecutors to be
     * used by the EPP.
     * @param pap the Policy Administration Point that the PDP will use to change the graph.
     * @throws PMException if there is an error initializing the EPP.
     */
    private PDP(Features pap, Decider decider, Auditor auditor) throws PMException {
        this.pap = pap;
        this.decider = decider;
        this.auditor = auditor;
    }

    private void setEPP(EPP epp) {
        this.epp = epp;
    }

    public EPP getEPP() {
        return epp;
    }

    public GraphService getGraphService(UserContext userCtx) throws PMException {
        return new GraphService(userCtx, pap, epp, decider, auditor);
    }

    public ProhibitionsService getProhibitionsService(UserContext userCtx) {
        return new ProhibitionsService(userCtx, pap, epp, decider, auditor);
    }

    public ObligationsService getObligationsService(UserContext userCtx) {
        return new ObligationsService(userCtx, pap, epp, decider, auditor);
    }

    public AnalyticsService getAnalyticsService(UserContext userCtx) {
        return new AnalyticsService(userCtx, pap, epp, decider, auditor);
    }

    public void runTx(UserContext userCtx, TxRunner txRunner) throws PMException {
        GraphService graphService = new GraphService(userCtx, pap, epp, decider, auditor);
        ProhibitionsService prohibitionsService = new ProhibitionsService(userCtx, pap, epp, decider, auditor);
        ObligationsService obligationsService = new ObligationsService(userCtx, pap, epp, decider, auditor);
        txRunner.run(graphService, prohibitionsService, obligationsService);
    }
}