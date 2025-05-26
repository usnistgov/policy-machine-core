package gov.nist.csd.pm.core.pdp;

import gov.nist.csd.pm.core.common.exception.PMException;

public interface PDPTxRunner<T> {
    T run(PDPTx policy) throws PMException;
}
