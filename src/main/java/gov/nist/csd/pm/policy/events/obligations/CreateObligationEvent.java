package gov.nist.csd.pm.policy.events.obligations;

import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.events.PolicyEvent;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.model.obligation.Rule;

import java.util.List;
import java.util.Objects;

public class CreateObligationEvent extends ObligationsEvent {

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

    @Override
    public void apply(Policy policy) throws PMException {
        policy.obligations().create(author, label, rules.toArray(Rule[]::new));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreateObligationEvent that = (CreateObligationEvent) o;
        return Objects.equals(author, that.author) && Objects.equals(label, that.label) && Objects.equals(rules, that.rules);
    }

    @Override
    public int hashCode() {
        return Objects.hash(author, label, rules);
    }
}
