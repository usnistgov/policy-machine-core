package gov.nist.csd.pm.pap.query;

import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.pap.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.pap.op.Operation;

import java.util.Collection;

public interface OperationsQuery {

    AccessRightSet getResourceOperations() throws PMException;
    Collection<String> getAdminOperationNames() throws PMException;
    Operation<?> getAdminOperation(String operationName) throws PMException;

}
