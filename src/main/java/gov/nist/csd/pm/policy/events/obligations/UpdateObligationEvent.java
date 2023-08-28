package gov.nist.csd.pm.policy.events.obligations;

import gov.nist.csd.pm.policy.events.PolicyEvent;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.model.obligation.Rule;

import java.util.List;
import java.util.Objects;

public class UpdateObligationEvent implements PolicyEvent {

    private UserContext author;
    private String name;
    private List<Rule> rules;

    public UpdateObligationEvent(UserContext author, String name, List<Rule> rules) {
        this.author = author;
        this.name = name;
        this.rules = rules;
    }

    public UserContext getAuthor() {
        return author;
    }

    public String getName() {
        return name;
    }

    public List<Rule> getRules() {
        return rules;
    }

    @Override
    public String getEventName() {
        return "update_obligation";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UpdateObligationEvent that = (UpdateObligationEvent) o;
        return Objects.equals(author, that.author) && Objects.equals(name, that.name) && Objects.equals(rules, that.rules);
    }

    @Override
    public int hashCode() {
        return Objects.hash(author, name, rules);
    }
}
