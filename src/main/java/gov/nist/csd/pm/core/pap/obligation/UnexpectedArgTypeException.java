package gov.nist.csd.pm.core.pap.obligation;

import gov.nist.csd.pm.core.common.exception.PMException;

public class UnexpectedArgTypeException extends PMException {
    public UnexpectedArgTypeException(Class<?> c) {
        super("unexpected arg type " + c.getName() + ", expected String or Collection<String>");
    }
}
