package gov.nist.csd.pm.pap.serialization;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.query.PolicyQuery;

public interface PolicySerializer {

    String serialize(PolicyQuery policyQuery) throws PMException;

}
