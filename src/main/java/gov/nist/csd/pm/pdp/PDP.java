package gov.nist.csd.pm.pdp;

import gov.nist.csd.pm.epp.EPP;
import gov.nist.csd.pm.epp.EPPOptions;
import gov.nist.csd.pm.epp.functions.FunctionExecutor;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pdp.services.*;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.graph.GraphSerializer;
import gov.nist.csd.pm.pip.obligations.Obligations;
import gov.nist.csd.pm.pip.prohibitions.Prohibitions;

public class PDP {

    private EPP epp;
    private PAP pap;

    private GraphService graphService;
    private ProhibitionsService prohibitionsService;
    private AnalyticsService    analyticsService;
    private ObligationsService obligationsService;
    private SuperPolicy superPolicy;

    /**
     * Create a new PDP instance given a Policy Administration Point and an optional set of FunctionExecutors to be
     * used by the EPP.
     * @param pap the Policy Administration Point that the PDP will use to change the graph.
     * @param eppOptions an optional list of external functions to be used by the PDP's EPP at runtime.
     * @throws PMException if there is an error initializing the EPP.
     */
    public PDP(PAP pap, EPPOptions eppOptions) throws PMException {
        this.pap = pap;
        // configure the super policy
        this.superPolicy = new SuperPolicy();
        this.superPolicy.configure(this.pap.getGraphPAP());

        this.epp = new EPP(this, eppOptions);

        // initialize services
        this.graphService = new GraphService(this.pap, this.epp, superPolicy);
        this.prohibitionsService = new ProhibitionsService(this.pap, this.epp, superPolicy);
        this.analyticsService = new AnalyticsService(this.pap, this.epp, superPolicy);
        this.obligationsService = new ObligationsService(this.pap, this.epp, superPolicy);
    }

    public EPP getEPP() {
        return epp;
    }

    public PAP getPAP() {
        return pap;
    }

    public Graph getGraphService(UserContext userCtx) throws PMException {
        graphService.setUserCtx(userCtx);
        return graphService;
    }

    public Prohibitions getProhibitionsService() throws PMException {
        return prohibitionsService;
    }

    public AnalyticsService getAnalyticsService() throws PMException {
        return analyticsService;
    }

    public Obligations getObligationsService() throws PMException {
        return obligationsService;
    }
}