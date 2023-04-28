package gov.nist.csd.pm.policy;

import gov.nist.csd.pm.policy.exceptions.PMException;

public interface PolicySerializer {

    String toJSON() throws PMException;

    String toPML() throws PMException;

}
