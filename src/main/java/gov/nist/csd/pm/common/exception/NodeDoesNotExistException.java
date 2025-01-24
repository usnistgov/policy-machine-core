package gov.nist.csd.pm.common.exception;

public class NodeDoesNotExistException extends PMException {
    public NodeDoesNotExistException(long id) {
        super("a node with the id \"" + id + "\" does not exist");
    }
    public NodeDoesNotExistException(String name) {
        super("a node with the name \"" + name + "\" does not exist");
    }
}
