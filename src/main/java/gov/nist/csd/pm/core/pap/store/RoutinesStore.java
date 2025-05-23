package gov.nist.csd.pm.core.pap.store;

import gov.nist.csd.pm.core.common.tx.Transactional;
import gov.nist.csd.pm.core.pap.modification.RoutinesModification;
import gov.nist.csd.pm.core.pap.query.RoutinesQuery;

public interface RoutinesStore extends Transactional, RoutinesModification, RoutinesQuery {

}
