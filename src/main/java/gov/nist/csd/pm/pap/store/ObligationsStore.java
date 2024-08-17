package gov.nist.csd.pm.pap.store;

import gov.nist.csd.pm.pap.tx.Transactional;
import gov.nist.csd.pm.pap.modification.ObligationsModification;
import gov.nist.csd.pm.pap.query.ObligationsQuery;

public interface ObligationsStore extends ObligationsModification, ObligationsQuery, Transactional {


}
