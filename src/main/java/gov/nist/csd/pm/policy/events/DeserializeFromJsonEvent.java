package gov.nist.csd.pm.policy.events;

import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.exceptions.PMException;

public class DeserializeFromJsonEvent implements PolicyEvent{

    private String json;

    public DeserializeFromJsonEvent(String json) {
        this.json = json;
    }

    @Override
    public String getEventName() {
        return "deserialize_from_json";
    }

    @Override
    public void apply(Policy policy) throws PMException {
        policy.deserialize().fromJSON(json);
    }
}
