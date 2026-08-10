package gov.nist.ngac.pm.core.pdp.bootstrap;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.PAP;

/**
 * Builds a starting policy for a {@link PAP}.
 */
public abstract class PolicyBootstrapper {

    public PolicyBootstrapper() {

    }

    /**
     * Applies this bootstrapper's policy to the given PAP.
     */
    public abstract void bootstrap(PAP pap) throws PMException;

}
