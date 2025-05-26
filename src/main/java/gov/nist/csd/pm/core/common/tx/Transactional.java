package gov.nist.csd.pm.core.common.tx;

import gov.nist.csd.pm.core.common.exception.PMException;

public interface Transactional {

    void beginTx() throws PMException;
    void commit() throws PMException;
    void rollback() throws PMException;

}
