package gov.nist.csd.pm.core.pap.serialization;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.query.PolicyQuery;

public interface PolicySerializer {

    String serialize(PolicyQuery policyQuery) throws PMException;

}
