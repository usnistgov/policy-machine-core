package gov.nist.csd.pm.core.pap.store;

import gov.nist.csd.pm.core.common.tx.Transactional;
import gov.nist.csd.pm.core.pap.modification.OperationsModification;
import gov.nist.csd.pm.core.pap.query.OperationsQuery;

public interface OperationsStore extends OperationsModification, OperationsQuery, Transactional {

}
