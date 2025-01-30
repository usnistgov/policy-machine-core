package gov.nist.csd.pm.pdp.adjudication;

import gov.nist.csd.pm.common.op.Operation;
import gov.nist.csd.pm.pap.PrivilegeChecker;

import java.util.List;

import static gov.nist.csd.pm.pap.AdminAccessRights.REVIEW_POLICY;

public abstract class Adjudicator {

    public static final List<String> TO_CHECK = List.of(REVIEW_POLICY);
    protected PrivilegeChecker privilegeChecker;

    public Adjudicator(PrivilegeChecker privilegeChecker) {
        this.privilegeChecker = privilegeChecker;
    }

}
