package gov.nist.csd.pm.pip.tx.memory.cmd;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pip.graph.Graph;

public interface TxCmd {

    void commit() throws PMException;

    void rollback() throws PMException;
}
