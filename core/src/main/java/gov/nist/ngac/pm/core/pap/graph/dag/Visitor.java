package gov.nist.ngac.pm.core.pap.graph.dag;

import gov.nist.ngac.pm.core.common.exception.PMException;

public interface Visitor {

    void visit(long node) throws PMException;
}
