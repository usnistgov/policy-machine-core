package gov.nist.csd.pm.core.pdp.adjudication;

import gov.nist.csd.pm.core.pap.function.arg.Args;
import gov.nist.csd.pm.core.pap.function.op.Operation;
import java.util.Map;

public record OperationRequest(String op, Map<String, Object> args) {
}
