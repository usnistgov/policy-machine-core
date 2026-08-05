package gov.nist.csd.pm.core.impl.memory.pap.store;

public class ConcurrentTxException extends IllegalStateException {

    public ConcurrentTxException() {
        super("in-memory policy store does not support concurrent transactions");
    }

}
