package gov.nist.csd.pm.core.common.exception;

public class NodeNameExistsException extends PMException {
    public NodeNameExistsException(String nodeName) {
        super("a node with the name " + nodeName + " already exists");
    }
}
