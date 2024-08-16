package gov.nist.csd.pm.pap.obligation;

import gov.nist.csd.pm.pap.exception.PMException;

public class UnexpectedOperandTypeException extends PMException {
    public UnexpectedOperandTypeException(Class<?> c) {
        super("unexpected operand type " + c.getName() + ", expected String or Collection<String>");
    }
}
