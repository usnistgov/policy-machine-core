package gov.nist.csd.pm.policy.events;

import java.io.Serializable;
import java.util.List;

public record PolicyEventList(List<PolicyEvent> events) implements Serializable { }
