package gov.nist.csd.pm.policy.events;

public class DissociateEvent extends PolicyEvent {

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
}
