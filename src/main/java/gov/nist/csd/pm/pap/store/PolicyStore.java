package gov.nist.csd.pm.pap.store;

import gov.nist.csd.pm.pap.tx.Transactional;

public interface PolicyStore extends Transactional {

    GraphStore graph();
    ProhibitionsStore prohibitions();
    ObligationsStore obligations();
    OperationsStore operations();
    RoutinesStore routines();

    void reset();

}
