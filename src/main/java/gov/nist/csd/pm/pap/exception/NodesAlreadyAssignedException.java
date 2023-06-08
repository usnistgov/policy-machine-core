package gov.nist.csd.pm.pap.exception;

import gov.nist.csd.pm.common.exception.PMException;

public class NodesAlreadyAssignedException extends PMException {
    public NodesAlreadyAssignedException(String ua, String target) {
        super("cannot associate \"" + ua + "\" with \"" + target + "\", \"" + ua + "\" is already assigned to \"" + target + "\"");
    }
}
