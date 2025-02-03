package gov.nist.csd.pm.pdp.bootstrap;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;

public interface PolicyBootstrapper {

    void bootstrap(PAP pap) throws PMException;

}
