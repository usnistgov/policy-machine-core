package gov.nist.csd.pm.core.common.exception;

public class NodeReferencedInProhibitionException extends PMException {
    public NodeReferencedInProhibitionException(String nodeToDelete, String prohibitionName) {
        super("cannot delete \"" + nodeToDelete + "\" because it is referenced in prohibition \"" + prohibitionName + "\"");
    }
}
