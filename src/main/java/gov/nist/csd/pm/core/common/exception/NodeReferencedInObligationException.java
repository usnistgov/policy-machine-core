package gov.nist.csd.pm.core.common.exception;

import java.util.Collection;

public class NodeReferencedInObligationException extends PMException {

    public NodeReferencedInObligationException(String nodeToDelete, Collection<String> obligations) {
        super("cannot delete \"" + nodeToDelete + "\" because it is referenced in obligations " + obligations);
    }
}
