package gov.nist.csd.pm.pap.exception;

public class NodeDoesNotExistException extends PMException {
    public NodeDoesNotExistException(String name) {
        super("a node with the name \"" + name + "\" does not exist");
    }
}
