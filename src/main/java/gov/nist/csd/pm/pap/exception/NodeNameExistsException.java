package gov.nist.csd.pm.pap.exception;

public class NodeNameExistsException extends PMException {
    public NodeNameExistsException(String nodeName) {
        super("a node with the name " + nodeName + " already exists");
    }
}
