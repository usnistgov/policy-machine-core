package gov.nist.ngac.pm.core.pdp;

import gov.nist.ngac.pm.core.common.exception.PMException;

/**
 * Callback invoked with the active {@link PDPTx} by {@link PDP#runTx}.
 *
 * @param <T> the callback's result type
 */
public interface PDPTxRunner<T> {
    T run(PDPTx policy) throws PMException;
}
