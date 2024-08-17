package gov.nist.csd.pm.pap.exception;

import java.util.Collection;

public class OperandsDoNotMatchException extends PMException {
    public OperandsDoNotMatchException(String opName, Collection<String> expected, Collection<String> actual) {
        super("operation " + opName + " expected operands " + expected + ", got " + actual);
    }
}
