package gov.nist.csd.pm.core.pap.graph.dag;

import gov.nist.csd.pm.core.common.exception.PMException;

public interface ShortCircuit {

    boolean evaluate(long nodeId) throws PMException;

}
