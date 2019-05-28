package gov.nist.csd.pm.pap;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.obligations.Obligations;
import gov.nist.csd.pm.pip.prohibitions.Prohibitions;

/**
 * PAP is the Policy Information Point. The purpose of the PAP is to expose the underlying policy data to the PDP and EPP.
 * It initializes the backend using the connection properties in /resource/db.config.  This servlet can
 * be access via ../index.jsp upon starting the server.The PAP also stores the in memory graph that will be used for
 * decision making.
 */
public class PAP {

    private Graph           graphPAP;
    private Prohibitions prohibitionsPAP;
    private Obligations     obligationsPAP;

    public PAP(Graph graphPAP, Prohibitions prohibitionsPAP, Obligations obligationsPAP) throws PMException {
        this.graphPAP = graphPAP;
        SuperGraph.check(this.graphPAP);

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
