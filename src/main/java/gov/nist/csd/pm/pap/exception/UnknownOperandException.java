package gov.nist.csd.pm.pap.exception;

import gov.nist.csd.pm.common.exception.PMException;

public class UnknownOperandException extends PMException {
    public UnknownOperandException(String operand) {
        super("unknown operand " + operand);
    }
}
