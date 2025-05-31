package gov.nist.csd.pm.core.pdp.query;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.core.pap.function.op.Operation;
import gov.nist.csd.pm.core.pap.admin.AdminAccessRights;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.function.op.PrivilegeChecker;
import gov.nist.csd.pm.core.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.core.pap.query.OperationsQuery;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.pdp.adjudication.Adjudicator;

import java.util.Collection;

public class OperationsQueryAdjudicator extends Adjudicator implements OperationsQuery {

    public OperationsQueryAdjudicator(PAP pap, UserContext userCtx) {
        super(pap, userCtx);
        this.userCtx = userCtx;
        this.pap = pap;
    }

    @Override
    public AccessRightSet getResourceOperations() throws PMException {
        pap.privilegeChecker().check(userCtx, AdminPolicyNode.PM_ADMIN_OPERATIONS.nodeId(), AdminAccessRights.QUERY_RESOURCE_OPERATIONS);

        return pap.query().operations().getResourceOperations();
    }

    @Override
    public Collection<String> getAdminOperationNames() throws PMException {
        pap.privilegeChecker().check(userCtx, AdminPolicyNode.PM_ADMIN_OPERATIONS.nodeId(), AdminAccessRights.QUERY_ADMIN_OPERATIONS);

        return pap.query().operations().getAdminOperationNames();
    }

    @Override
    public Operation<?, ?> getAdminOperation(String operationName) throws PMException {
        pap.privilegeChecker().check(userCtx, AdminPolicyNode.PM_ADMIN_OPERATIONS.nodeId(), AdminAccessRights.QUERY_ADMIN_OPERATIONS);

        return pap.query().operations().getAdminOperation(operationName);
    }
}
