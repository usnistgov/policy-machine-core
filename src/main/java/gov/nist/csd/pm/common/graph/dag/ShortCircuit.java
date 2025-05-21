package gov.nist.csd.pm.common.graph.dag;

import gov.nist.csd.pm.common.exception.PMException;

public interface ShortCircuit {

    boolean evaluate(long nodeId) throws PMException;

}
