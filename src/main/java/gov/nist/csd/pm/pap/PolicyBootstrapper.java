package gov.nist.csd.pm.pap;

import gov.nist.csd.pm.policy.exceptions.PMException;

public interface PolicyBootstrapper {

    void bootstrap(PAP pap) throws PMException;

}
