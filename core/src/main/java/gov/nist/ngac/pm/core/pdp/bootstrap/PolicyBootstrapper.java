package gov.nist.ngac.pm.core.pdp.bootstrap;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.PAP;

public abstract class PolicyBootstrapper {

    public PolicyBootstrapper() {

    }

    public abstract void bootstrap(PAP pap) throws PMException;

}
