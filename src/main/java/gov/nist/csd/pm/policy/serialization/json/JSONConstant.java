package gov.nist.csd.pm.policy.serialization.json;

import gov.nist.csd.pm.policy.pml.value.Value;

public class JSONConstant {
    private final Class<? extends Value> valueClass;
    private final String value;

    public JSONConstant(Class<? extends Value> valueClass, String value) {
        this.valueClass = valueClass;
        this.value = value;
    }

    public Class<? extends Value> valueClass() {
        return valueClass;
    }

    public String value() {
        return value;
    }
}
