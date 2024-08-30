package gov.nist.csd.pm.pap;

import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.pap.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.pap.exception.OperationDoesNotExistException;
import gov.nist.csd.pm.pap.op.Operation;
import gov.nist.csd.pm.pap.query.OperationsQuery;
import gov.nist.csd.pm.pap.store.PolicyStore;

import java.util.*;

public class OperationsQuerier extends Querier implements OperationsQuery {

    public OperationsQuerier(PolicyStore store) {
        super(store);
    }

    public OperationsQuerier(Querier querier) {
        super(querier);
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
    public Operation<?> getAdminOperation(String operationName) throws PMException {
        if (!store.operations().getAdminOperationNames().contains(operationName)) {
            throw new OperationDoesNotExistException(operationName);
        }

        return store.operations().getAdminOperation(operationName);
    }
}
