package gov.nist.csd.pm.core.pdp.adjudication;

import java.util.Map;

public record OperationRequest(String op, Map<String, Object> args) {
}
