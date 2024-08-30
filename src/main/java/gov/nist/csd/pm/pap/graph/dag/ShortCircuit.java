package gov.nist.csd.pm.pap.graph.dag;

import gov.nist.csd.pm.pap.exception.PMException;

public interface ShortCircuit {

    boolean evaluate(String node) throws PMException;

}
