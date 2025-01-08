package gov.nist.csd.pm.pap.store;

import gov.nist.csd.pm.common.tx.Transactional;
import gov.nist.csd.pm.pap.modification.RoutinesModification;
import gov.nist.csd.pm.pap.query.RoutinesQuery;

public interface RoutinesStore extends Transactional, RoutinesModification, RoutinesQuery {

}
