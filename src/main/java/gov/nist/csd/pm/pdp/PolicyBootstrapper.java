package gov.nist.csd.pm.pdp;

import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.policy.exceptions.PMException;

public interface PolicyBootstrapper {

    void bootstrap(PAP pap) throws PMException;

}
