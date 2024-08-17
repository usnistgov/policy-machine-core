package gov.nist.csd.pm.pap.exception;

public class UnknownOperandException extends PMException {
    public UnknownOperandException(String operand) {
        super("unknown operand " + operand);
    }
}
