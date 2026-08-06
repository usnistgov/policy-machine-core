package gov.nist.ngac.pm.core.pdp.adjudication;

import java.util.Map;

/**
 * A single operation invocation to adjudicate, as part of a routine's batch.
 *
 * @param op the operation name
 * @param args the operation's argument values, keyed by parameter name
 */
public record OperationRequest(String op, Map<String, Object> args) {
}
