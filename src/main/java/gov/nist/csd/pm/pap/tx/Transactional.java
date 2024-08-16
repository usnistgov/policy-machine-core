package gov.nist.csd.pm.pap.tx;

import gov.nist.csd.pm.pap.exception.PMException;

public interface Transactional {

    void beginTx() throws PMException;
    void commit() throws PMException;
    void rollback() throws PMException;

}
