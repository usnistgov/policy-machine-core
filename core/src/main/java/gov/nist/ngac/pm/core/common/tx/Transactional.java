package gov.nist.ngac.pm.core.common.tx;

import gov.nist.ngac.pm.core.common.exception.PMException;

public interface Transactional {

    void beginTx() throws PMException;
    void commit() throws PMException;
    void rollback() throws PMException;

}
