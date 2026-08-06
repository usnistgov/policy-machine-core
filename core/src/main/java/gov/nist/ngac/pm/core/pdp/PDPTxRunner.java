package gov.nist.ngac.pm.core.pdp;

import gov.nist.ngac.pm.core.common.exception.PMException;

/**
 * Callback invoked with the active {@link PDPTx} by {@link PDP#runTx}.
 */
public interface PDPTxRunner<T> {
    T run(PDPTx policy) throws PMException;
}
