package gov.nist.csd.pm.pdp;

import gov.nist.csd.pm.pap.op.PrivilegeChecker;

import java.util.List;

import static gov.nist.csd.pm.pap.op.AdminAccessRights.REVIEW_POLICY;

public abstract class Adjudicator {

    public static final List<String> TO_CHECK = List.of(REVIEW_POLICY);
    protected PrivilegeChecker privilegeChecker;

    public Adjudicator(PrivilegeChecker privilegeChecker) {
        this.privilegeChecker = privilegeChecker;
    }
}
