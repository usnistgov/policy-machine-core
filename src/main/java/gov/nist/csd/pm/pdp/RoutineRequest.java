package gov.nist.csd.pm.pdp;

import java.util.Map;

public record RoutineRequest(String name, Map<String, Object> operands) {
}
