package gov.nist.csd.pm.common.obligation;

import gov.nist.csd.pm.common.exception.PMException;

public class UnexpectedOperandTypeException extends PMException {
    public UnexpectedOperandTypeException(Class<?> c) {
        super("unexpected operand type " + c.getName() + ", expected String or Collection<String>");
    }
}
