package gov.nist.csd.pm.policy.exceptions;

public class NodeReferencedInProhibitionException extends PMException{
    public NodeReferencedInProhibitionException(String nodeToDelete, String prohibitionLabel) {
        super("cannot delete \"" + nodeToDelete + "\" because it is referenced in prohibition \"" + prohibitionLabel + "\"");
    }
}
