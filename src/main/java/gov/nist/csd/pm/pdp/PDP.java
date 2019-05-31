package gov.nist.csd.pm.pdp;

import gov.nist.csd.pm.epp.EPP;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pdp.services.AnalyticsService;
import gov.nist.csd.pm.pdp.services.GraphService;
import gov.nist.csd.pm.pdp.services.ObligationsService;
import gov.nist.csd.pm.pdp.services.ProhibitionsService;

public class PDP {

    private EPP epp;
    private PAP pap;

    private GraphService        graphService;
    private ProhibitionsService prohibitionsService;
    private AnalyticsService    analyticsService;
    private ObligationsService  obligationsService;

    public PDP(PAP pap) {
        this.pap = pap;
        this.epp = new EPP(this);

        this.graphService = new GraphService(this.pap, this.epp);
        this.prohibitionsService = new ProhibitionsService(this.pap, this.epp);
        this.analyticsService = new AnalyticsService(this.pap, this.epp);
        this.obligationsService = new ObligationsService(this.pap, this.epp);
    }

    public PDP(PAP pap, EPP epp) {
        this.pap = pap;
        this.epp = epp;
    }

    public void setEPP(EPP epp) {
        this.epp = epp;
    }

    public EPP getEPP() {
        return epp;
    }

    public PAP getPAP() {
        return pap;
    }

    public GraphService getGraphService() throws PMException {
        return graphService;
    }

    public ProhibitionsService getProhibitionsService() throws PMException {
        return prohibitionsService;
    }

    public AnalyticsService getAnalyticsService() throws PMException {
        return analyticsService;
    }

    public ObligationsService getObligationsService() throws PMException {
        return obligationsService;
    }
}