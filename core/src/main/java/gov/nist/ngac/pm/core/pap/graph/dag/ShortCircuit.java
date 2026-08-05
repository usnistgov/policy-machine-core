package gov.nist.ngac.pm.core.pap.graph.dag;

import gov.nist.ngac.pm.core.common.exception.PMException;

public interface ShortCircuit {

    boolean evaluate(long nodeId) throws PMException;

}
