package gov.nist.csd.pm.pdp.query;

import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.pap.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.pap.op.AdminAccessRights;
import gov.nist.csd.pm.pap.op.Operation;
import gov.nist.csd.pm.pap.op.PrivilegeChecker;
import gov.nist.csd.pm.pap.query.OperationsQuery;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.pdp.Adjudicator;

import java.util.Collection;

public class OperationsQueryAdjudicator extends Adjudicator implements OperationsQuery {

    private UserContext userCtx;
    private PAP pap;
    private final PrivilegeChecker privilegeChecker;

    public OperationsQueryAdjudicator(UserContext userCtx, PAP pap, PrivilegeChecker privilegeChecker) {
        super(privilegeChecker);
        this.userCtx = userCtx;
        this.pap = pap;
        this.privilegeChecker = privilegeChecker;
    }

    @Override
    public AccessRightSet getResourceOperations() throws PMException {
        privilegeChecker.check(userCtx, AdminPolicyNode.PM_ADMIN_OBJECT.nodeName(), AdminAccessRights.REVIEW_POLICY);

        return pap.query().operations().getResourceOperations();
    }

    @Override
    public Collection<String> getAdminOperationNames() throws PMException {
        privilegeChecker.check(userCtx, AdminPolicyNode.PM_ADMIN_OBJECT.nodeName(), AdminAccessRights.REVIEW_POLICY);

        return pap.query().operations().getAdminOperationNames();
    }

    @Override
    public Operation<?> getAdminOperation(String operationName) throws PMException {
        privilegeChecker.check(userCtx, AdminPolicyNode.PM_ADMIN_OBJECT.nodeName(), AdminAccessRights.REVIEW_POLICY);

        return pap.query().operations().getAdminOperation(operationName);
    }
}
