package gov.nist.csd.pm.core.pdp.query;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.core.pap.function.op.Operation;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.query.OperationsQuery;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.pdp.adjudication.Adjudicator;

import java.util.Collection;

/**
 * Adjudicate queries on operations. Because operations are not Policy Elements, there are no access checks on
 * querying them. The access checks are enforced on operation execution.
 */
public class OperationsQueryAdjudicator extends Adjudicator implements OperationsQuery {

    public OperationsQueryAdjudicator(PAP pap, UserContext userCtx) {
        super(pap, userCtx);
        this.userCtx = userCtx;
        this.pap = pap;
    }

    @Override
    public AccessRightSet getResourceOperations() throws PMException {
        return pap.query().operations().getResourceOperations();
    }

    @Override
    public Collection<String> getAdminOperationNames() throws PMException {
        return pap.query().operations().getAdminOperationNames();
    }

    @Override
    public Operation<?> getAdminOperation(String operationName) throws PMException {
        return pap.query().operations().getAdminOperation(operationName);
    }
}
