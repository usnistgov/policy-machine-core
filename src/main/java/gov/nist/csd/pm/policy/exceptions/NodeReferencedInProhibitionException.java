package gov.nist.csd.pm.policy.exceptions;

public class NodeReferencedInProhibitionException extends PMException{
    public NodeReferencedInProhibitionException(String nodeToDelete, String prohibitionName) {
        super("cannot delete \"" + nodeToDelete + "\" because it is referenced in prohibition \"" + prohibitionName + "\"");
    }
}
