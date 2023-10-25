package gov.nist.csd.pm.policy.tx;

import gov.nist.csd.pm.policy.exceptions.PMException;

public interface Transactional {

    void beginTx() throws PMException;
    void commit() throws PMException;
    void rollback() throws PMException;

}
