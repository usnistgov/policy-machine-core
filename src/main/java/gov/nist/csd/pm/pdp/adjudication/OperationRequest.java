package gov.nist.csd.pm.pdp.adjudication;

import gov.nist.csd.pm.pap.function.arg.Args;
import gov.nist.csd.pm.pap.function.op.Operation;
import java.util.Map;

public record OperationRequest(String op, Map<String, Object> args) {
}
