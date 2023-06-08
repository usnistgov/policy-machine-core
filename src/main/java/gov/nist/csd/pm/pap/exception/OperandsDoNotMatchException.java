package gov.nist.csd.pm.pap.exception;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.op.Operation;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class OperandsDoNotMatchException extends PMException {
    public OperandsDoNotMatchException(String opName, Collection<String> expected, Collection<String> actual) {
        super("operation " + opName + " expected operands " + expected + ", got " + actual);
    }
}
