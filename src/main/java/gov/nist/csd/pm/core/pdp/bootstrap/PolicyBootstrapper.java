package gov.nist.csd.pm.core.pdp.bootstrap;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;

public abstract class PolicyBootstrapper {

    public PolicyBootstrapper() {

    }

    public abstract void bootstrap(PAP pap) throws PMException;

}
