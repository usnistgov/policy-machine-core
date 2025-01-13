package gov.nist.csd.pm.pdp.bootstrap;

import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.common.exception.PMException;

public interface PolicyBootstrapper {

    void bootstrap(PAP pap) throws PMException;

}
