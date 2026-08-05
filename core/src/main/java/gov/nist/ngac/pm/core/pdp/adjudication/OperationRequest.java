package gov.nist.ngac.pm.core.pdp.adjudication;

import java.util.Map;

public record OperationRequest(String op, Map<String, Object> args) {
}
