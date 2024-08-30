package gov.nist.csd.pm.pdp;

import gov.nist.csd.pm.pap.op.PrivilegeChecker;

public abstract class Adjudicator {

    protected PrivilegeChecker privilegeChecker;

    public Adjudicator(PrivilegeChecker privilegeChecker) {
        this.privilegeChecker = privilegeChecker;
    }
}
