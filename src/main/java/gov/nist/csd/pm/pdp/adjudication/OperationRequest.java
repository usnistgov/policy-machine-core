package gov.nist.csd.pm.pdp.adjudication;

import gov.nist.csd.pm.pap.executable.arg.ActualArgs;
import gov.nist.csd.pm.pap.executable.op.Operation;

public record OperationRequest(Operation<?> op, ActualArgs args) {
}
