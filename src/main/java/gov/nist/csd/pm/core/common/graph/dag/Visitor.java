package gov.nist.csd.pm.core.common.graph.dag;

import gov.nist.csd.pm.core.common.exception.PMException;

public interface Visitor {

    void visit(long node) throws PMException;
}
