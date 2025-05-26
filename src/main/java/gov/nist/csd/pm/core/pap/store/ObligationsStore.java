package gov.nist.csd.pm.core.pap.store;

import gov.nist.csd.pm.core.common.tx.Transactional;
import gov.nist.csd.pm.core.pap.modification.ObligationsModification;
import gov.nist.csd.pm.core.pap.query.ObligationsQuery;

public interface ObligationsStore extends ObligationsModification, ObligationsQuery, Transactional {


}
