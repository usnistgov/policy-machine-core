package gov.nist.csd.pm.pdp;

import gov.nist.csd.pm.common.exception.PMException;

public interface PDPTxRunner {
    Object run(PDPTx policy) throws PMException;
}
