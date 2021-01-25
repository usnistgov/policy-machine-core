package gov.nist.csd.pm.pap;

import gov.nist.csd.pm.exceptions.PMException;

public class PAP {

    private GraphAdmin        graphAdmin;
    private ProhibitionsAdmin prohibitionsAdmin;
    private ObligationsAdmin  obligationsAdmin;

    public PAP(GraphAdmin graphAdmin, ProhibitionsAdmin prohibitionsAdmin, ObligationsAdmin obligationsAdmin) throws PMException {
        this.graphAdmin = graphAdmin;
        this.prohibitionsAdmin = prohibitionsAdmin;
        this.obligationsAdmin = obligationsAdmin;
    }

    public GraphAdmin getGraphAdmin() {
        return graphAdmin;
    }

    public void setGraphAdmin(GraphAdmin graphAdmin) {
        this.graphAdmin = graphAdmin;
    }

    public ProhibitionsAdmin getProhibitionsAdmin() {
        return prohibitionsAdmin;
    }

    public void setProhibitionsAdmin(ProhibitionsAdmin prohibitionsAdmin) {
        this.prohibitionsAdmin = prohibitionsAdmin;
    }

    public ObligationsAdmin getObligationsAdmin() {
        return obligationsAdmin;
    }

    public void setObligationsAdmin(ObligationsAdmin obligationsAdmin) {
        this.obligationsAdmin = obligationsAdmin;
    }
}
