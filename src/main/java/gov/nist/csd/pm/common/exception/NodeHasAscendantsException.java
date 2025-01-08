package gov.nist.csd.pm.common.exception;

public class NodeHasAscendantsException extends PMException {
    public NodeHasAscendantsException(String node) {
        super("cannot delete " + node + ", it has nodes assigned to it");
    }
}
