package gov.nist.csd.pm.common.tx;

import gov.nist.csd.pm.common.exception.PMException;

public interface Transactional {

    void beginTx() throws PMException;
    void commit() throws PMException;
    void rollback() throws PMException;

}
