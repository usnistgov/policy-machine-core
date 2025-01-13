package gov.nist.csd.pm.pdp.adjudication;

import java.util.Map;

public record OperationRequest(String name, Map<String, Object> operands) {
}
