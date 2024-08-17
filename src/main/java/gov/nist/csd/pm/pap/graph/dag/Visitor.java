package gov.nist.csd.pm.pap.graph.dag;

import gov.nist.csd.pm.pap.exception.PMException;

public interface Visitor {

    void visit(String node) throws PMException;
}
