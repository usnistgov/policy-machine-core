package gov.nist.csd.pm.core.pap.query;

import gov.nist.csd.pm.core.common.exception.OperationDoesNotExistException;
import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.core.pap.function.op.Operation;
import gov.nist.csd.pm.core.pap.store.PolicyStore;

import java.util.Collection;

public class OperationsQuerier extends Querier implements OperationsQuery {

    public OperationsQuerier(PolicyStore store) {
        super(store);
    }

    @Override
    public AccessRightSet getResourceOperations() throws PMException {
        return store.operations().getResourceOperations();
    }

    @Override
    public Collection<String> getAdminOperationNames() throws PMException {
        return store.operations().getAdminOperationNames();
    }

    @Override
    public Operation<?, ?> getAdminOperation(String operationName) throws PMException {
        if (!store.operations().getAdminOperationNames().contains(operationName)) {
            throw new OperationDoesNotExistException(operationName);
        }

        return store.operations().getAdminOperation(operationName);
    }
}
