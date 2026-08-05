package gov.nist.ngac.pm.core.pap.store;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.common.tx.Transactional;

public interface PolicyStore extends Transactional {

    GraphStore graph();
    ProhibitionsStore prohibitions();
    ObligationsStore obligations();
    OperationsStore operations();

    void reset() throws PMException;

}
