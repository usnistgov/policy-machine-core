package gov.nist.csd.pm.common.graph.dag;

import gov.nist.csd.pm.common.exception.PMException;

public interface Visitor {

    void visit(String node) throws PMException;
}
