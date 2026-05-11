package gov.nist.csd.pm.core.pap.graph.dag;

import gov.nist.csd.pm.core.common.exception.PMException;

public interface Visitor {

    void visit(long node) throws PMException;
}
