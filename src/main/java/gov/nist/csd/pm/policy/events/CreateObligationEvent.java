package gov.nist.csd.pm.policy.events;

import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.model.obligation.Rule;

import java.util.List;

public class CreateObligationEvent implements PolicyEvent {

    private final UserContext author;
    private final String label;
    private final List<Rule> rules;

    public CreateObligationEvent(UserContext author, String label, List<Rule> rules) {
        this.author = author;
        this.label = label;
        this.rules = rules;
    }

    public UserContext getAuthor() {
        return author;
    }

    public String getLabel() {
        return label;
    }

    public List<Rule> getRules() {
        return rules;
    }

    @Override
    public String getEventName() {
        return "create_obligation";
    }
}
