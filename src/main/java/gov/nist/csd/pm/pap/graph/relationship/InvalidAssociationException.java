package gov.nist.csd.pm.pap.graph.relationship;

import gov.nist.csd.pm.pap.exception.PMException;

public class InvalidAssociationException extends PMException {
    public InvalidAssociationException(String msg) {
        super(msg);
    }
}
