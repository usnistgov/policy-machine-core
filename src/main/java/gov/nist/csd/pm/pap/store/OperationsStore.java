package gov.nist.csd.pm.pap.store;

import gov.nist.csd.pm.common.tx.Transactional;
import gov.nist.csd.pm.pap.modification.OperationsModification;
import gov.nist.csd.pm.pap.query.OperationsQuery;

public interface OperationsStore extends OperationsModification, OperationsQuery, Transactional {

}
