package gov.nist.csd.pm.policy.events.graph;

import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.events.PolicyEvent;
import gov.nist.csd.pm.policy.exceptions.PMException;

import java.util.Objects;

public class DissociateEvent implements PolicyEvent {

    private final String ua;
    private final String target;

    public DissociateEvent(String ua, String target) {
        this.ua = ua;
        this.target = target;
    }

    public String getUa() {
        return ua;
    }

    public String getTarget() {
        return target;
    }

    @Override
    public String getEventName() {
        return "dissociate";
    }

    @Override
    public void apply(Policy policy) throws PMException {
        policy.graph().dissociate(ua, target);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DissociateEvent that = (DissociateEvent) o;
        return Objects.equals(ua, that.ua) && Objects.equals(target, that.target);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ua, target);
    }
}
