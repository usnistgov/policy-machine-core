package gov.nist.csd.pm.pdp.adjudication;

import gov.nist.csd.pm.pap.function.arg.Args;
import gov.nist.csd.pm.pap.function.op.Operation;

public record OperationRequest(Operation<?> op, Args args) {
}
