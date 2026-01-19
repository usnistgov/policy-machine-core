package gov.nist.csd.pm.core.pap.function;

import gov.nist.csd.pm.core.common.graph.relationship.AccessRightSet;
import java.io.Serializable;
import java.util.List;

/**
 * RequiredCapabilities represents the access rights needed on an argument of an operation.
 */
public class RequiredCapabilities implements Serializable {

    private AccessRightSet reqCaps;

    public RequiredCapabilities(AccessRightSet reqCaps) {
        this.reqCaps = reqCaps;
    }

    public RequiredCapabilities(String ... reqCaps) {
        this.reqCaps = new AccessRightSet(reqCaps);
    }

    public RequiredCapabilities(List<String> reqCaps) {
        this.reqCaps = new AccessRightSet(reqCaps);
    }

    public AccessRightSet getReqCaps() {
        return reqCaps;
    }

    public void setReqCaps(AccessRightSet reqCaps) {
        this.reqCaps = reqCaps;
    }
}
