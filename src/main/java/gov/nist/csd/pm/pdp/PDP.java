package gov.nist.csd.pm.pdp;

import gov.nist.csd.pm.epp.EPP;
import gov.nist.csd.pm.epp.EPPOptions;
import gov.nist.csd.pm.epp.functions.FunctionExecutor;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.operations.OperationSet;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pdp.policy.SuperPolicy;
import gov.nist.csd.pm.pdp.services.*;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.graph.GraphSerializer;
import gov.nist.csd.pm.pip.obligations.Obligations;
import gov.nist.csd.pm.pip.prohibitions.Prohibitions;

import java.util.Set;

public class PDP {

    private EPP epp;

    private GraphService graphService;
    private ProhibitionsService prohibitionsService;
    private AnalyticsService    analyticsService;
    private ObligationsService obligationsService;

    /**
     * Create a new PDP instance given a Policy Administration Point and an optional set of FunctionExecutors to be
     * used by the EPP.
     * @param pap the Policy Administration Point that the PDP will use to change the graph.
     * @param eppOptions an optional list of external functions to be used by the PDP's EPP at runtime.
     * @param resourceOps the set of operations that the PDP will understand while traversing the graph.
     * @throws PMException if there is an error initializing the EPP.
     */
    public PDP(PAP pap, EPPOptions eppOptions, OperationSet resourceOps) throws PMException {
        this.epp = new EPP(this, pap, eppOptions);

        // initialize services
        this.graphService = new GraphService(pap, this.epp, resourceOps);
        // configure the super policy
        SuperPolicy superPolicy = this.graphService.configureSuperPolicy();

        this.prohibitionsService = new ProhibitionsService(pap, this.epp, resourceOps, superPolicy);
        this.analyticsService = new AnalyticsService(pap, this.epp, resourceOps);
        this.obligationsService = new ObligationsService(pap, this.epp, resourceOps, superPolicy);
    }

    public EPP getEPP() {
        return epp;
    }

    public GraphService getGraphService (UserContext userCtx) {
        graphService.setUserCtx(userCtx);
        return graphService;
    }

    public Prohibitions getProhibitionsService (UserContext userCtx) {
        prohibitionsService.setUserCtx(userCtx);
        return prohibitionsService;
    }

    public AnalyticsService getAnalyticsService(UserContext userCtx) {
        analyticsService.setUserCtx(userCtx);
        return analyticsService;
    }

    public Obligations getObligationsService(UserContext userCtx) {
        obligationsService.setUserCtx(userCtx);
        return obligationsService;
    }
}
