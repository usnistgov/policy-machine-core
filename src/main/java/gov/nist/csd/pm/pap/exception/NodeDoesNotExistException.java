package gov.nist.csd.pm.pap.exception;

import gov.nist.csd.pm.common.exception.PMException;

public class NodeDoesNotExistException extends PMException {
    public NodeDoesNotExistException(String name) {
        super("a node with the name \"" + name + "\" does not exist");
    }
}
