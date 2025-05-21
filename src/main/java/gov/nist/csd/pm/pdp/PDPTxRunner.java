package gov.nist.csd.pm.pdp;

import gov.nist.csd.pm.common.exception.PMException;

public interface PDPTxRunner<T> {
    T run(PDPTx policy) throws PMException;
}
