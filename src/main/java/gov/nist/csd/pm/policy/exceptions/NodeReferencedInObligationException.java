package gov.nist.csd.pm.policy.exceptions;

public class NodeReferencedInObligationException extends PMException{

    public NodeReferencedInObligationException(String nodeToDelete, String obligationId) {
        super("cannot delete \"" + nodeToDelete + "\" because it is referenced in obligation \"" + obligationId + "\"");
    }
}
