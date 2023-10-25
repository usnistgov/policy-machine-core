package gov.nist.csd.pm.pap.serialization.json;

import gov.nist.csd.pm.policy.model.access.AccessRightSet;

public class JSONAssociation {

    private String target;
    private AccessRightSet arset;

    public JSONAssociation() {
        arset = new AccessRightSet();
    }

    public JSONAssociation(String target, AccessRightSet arset) {
        this.target = target;
        this.arset = arset;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public AccessRightSet getArset() {
        return arset;
    }

    public void setArset(AccessRightSet arset) {
        this.arset = arset;
    }
}
