package gov.nist.csd.pm.core.common.exception;

public class NodeReferencedInObligationException extends PMException {

    public NodeReferencedInObligationException(String nodeToDelete, String obligationName) {
        super("cannot delete \"" + nodeToDelete + "\" because it is referenced in obligation \"" + obligationName + "\"");
    }
}
