package gov.nist.csd.pm.common.exception;

public class UnknownOperandException extends PMException {
    public UnknownOperandException(String operand) {
        super("unknown operand " + operand);
    }
}
