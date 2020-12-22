package gov.nist.csd.pm.pip.memory.tx.cmd;

import gov.nist.csd.pm.exceptions.PMException;

public interface TxCmd {

    void commit() throws PMException;

}
