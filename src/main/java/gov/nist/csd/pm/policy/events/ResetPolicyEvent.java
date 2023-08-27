package gov.nist.csd.pm.policy.events;

public class ResetPolicyEvent implements PolicyEvent{
    @Override
    public String getEventName() {
        return "reset_policy";
    }

}
