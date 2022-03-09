package gov.nist.csd.pm.policy.model.graph.relationships;

import gov.nist.csd.pm.policy.exceptions.PMException;

public class InvalidAssociationException extends PMException {
    public InvalidAssociationException(String msg) {
        super(msg);
    }
}
