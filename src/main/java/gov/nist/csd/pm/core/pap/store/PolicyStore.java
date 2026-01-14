package gov.nist.csd.pm.core.pap.store;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.tx.Transactional;

public interface PolicyStore extends Transactional {

    GraphStore graph();
    ProhibitionsStore prohibitions();
    ObligationsStore obligations();
    OperationsStore operations();
    RoutinesStore routines();

    void reset() throws PMException;

}
