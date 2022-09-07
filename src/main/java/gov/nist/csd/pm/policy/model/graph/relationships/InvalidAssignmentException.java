package gov.nist.csd.pm.policy.model.graph.relationships;

import gov.nist.csd.pm.policy.exceptions.PMException;

public class InvalidAssignmentException extends PMException {
    public InvalidAssignmentException(String msg) {
        super(msg);
    }
}
