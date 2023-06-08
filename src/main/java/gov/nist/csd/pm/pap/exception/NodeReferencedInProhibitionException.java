package gov.nist.csd.pm.pap.exception;

import gov.nist.csd.pm.common.exception.PMException;

public class NodeReferencedInProhibitionException extends PMException {
    public NodeReferencedInProhibitionException(String nodeToDelete, String prohibitionName) {
        super("cannot delete \"" + nodeToDelete + "\" because it is referenced in prohibition \"" + prohibitionName + "\"");
    }
}
