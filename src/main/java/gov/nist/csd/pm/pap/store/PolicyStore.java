package gov.nist.csd.pm.pap.store;

import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.pap.tx.Transactional;

public interface PolicyStore extends Transactional {

    GraphStore graph() throws PMException;
    ProhibitionsStore prohibitions() throws PMException;
    ObligationsStore obligations() throws PMException;
    OperationsStore operations() throws PMException;
    RoutinesStore routines() throws PMException;

    void reset() throws PMException;

}
