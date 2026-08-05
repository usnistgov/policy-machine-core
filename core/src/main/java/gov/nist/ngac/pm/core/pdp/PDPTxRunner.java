package gov.nist.ngac.pm.core.pdp;

import gov.nist.ngac.pm.core.common.exception.PMException;

public interface PDPTxRunner<T> {
    T run(PDPTx policy) throws PMException;
}
