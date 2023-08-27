package gov.nist.csd.pm.policy.events.obligations;

import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.model.obligation.Rule;

import java.util.List;
import java.util.Objects;

public class CreateObligationEvent extends ObligationsEvent {

    private final UserContext author;
    private final String id;
    private final List<Rule> rules;

    public CreateObligationEvent(UserContext author, String id, List<Rule> rules) {
        this.author = author;
        this.id = id;
        this.rules = rules;
    }

    public UserContext getAuthor() {
        return author;
    }

    public String getId() {
        return id;
    }

    public List<Rule> getRules() {
        return rules;
    }

    @Override
    public String getEventName() {
        return "create_obligation";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreateObligationEvent that = (CreateObligationEvent) o;
        return Objects.equals(author, that.author) && Objects.equals(id, that.id) && Objects.equals(rules, that.rules);
    }

    @Override
    public int hashCode() {
        return Objects.hash(author, id, rules);
    }
}
