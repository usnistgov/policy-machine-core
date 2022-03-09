package gov.nist.csd.pm.policy.exceptions;

public class NodeHasChildrenException extends PMException{
    public NodeHasChildrenException(String node) {
        super("cannot delete " + node + ", it has nodes assigned to it");
    }
}
