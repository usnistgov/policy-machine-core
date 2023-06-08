package gov.nist.csd.pm.pap.exception;

import gov.nist.csd.pm.common.exception.PMException;

public class NodeNameExistsException extends PMException {
    public NodeNameExistsException(String nodeName) {
        super("a node with the name " + nodeName + " already exists");
    }
}
