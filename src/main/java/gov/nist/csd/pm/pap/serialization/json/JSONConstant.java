package gov.nist.csd.pm.pap.serialization.json;

import gov.nist.csd.pm.policy.pml.value.Value;

public record JSONConstant(Class<? extends Value> valueClass, String value) { }
