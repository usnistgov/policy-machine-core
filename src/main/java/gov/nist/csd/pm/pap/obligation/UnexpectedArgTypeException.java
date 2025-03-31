package gov.nist.csd.pm.pap.obligation;

import gov.nist.csd.pm.common.exception.PMException;

public class UnexpectedArgTypeException extends PMException {
    public UnexpectedArgTypeException(Class<?> c) {
        super("unexpected arg type " + c.getName() + ", expected String or Collection<String>");
    }
}
