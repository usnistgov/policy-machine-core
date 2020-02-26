package gov.nist.csd.pm.pap;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.obligations.Obligations;
import gov.nist.csd.pm.pip.prohibitions.Prohibitions;

public class PAP {

    private Graph        graphPAP;
    private Prohibitions prohibitionsPAP;
    private Obligations  obligationsPAP;

    public PAP(Graph graphPAP, Prohibitions prohibitionsPAP, Obligations obligationsPAP) throws PMException {
        this.graphPAP = graphPAP;
        this.prohibitionsPAP = prohibitionsPAP;
        this.obligationsPAP = obligationsPAP;
    }

    public Graph getGraphPAP() {
        return graphPAP;
    }

    public void setGraphPAP(Graph graphPAP) {
        this.graphPAP = graphPAP;
    }

    public Prohibitions getProhibitionsPAP() {
        return prohibitionsPAP;
    }

    public void setProhibitionsPAP(Prohibitions prohibitionsPAP) {
        this.prohibitionsPAP = prohibitionsPAP;
    }

    public Obligations getObligationsPAP() {
        return obligationsPAP;
    }

    public void setObligationsPAP(Obligations obligationsPAP) {
        this.obligationsPAP = obligationsPAP;
    }
}
