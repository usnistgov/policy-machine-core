package gov.nist.csd.pm.pap.exception;

import gov.nist.csd.pm.common.exception.PMException;

public class ObligationNameExistsException extends PMException {
    public ObligationNameExistsException(String name) {
        super("obligation with name " + name + " already exists");
    }
}
