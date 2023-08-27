package gov.nist.csd.pm.policy.model.obligation;

import gov.nist.csd.pm.policy.model.obligation.event.EventPattern;
import gov.nist.csd.pm.policy.model.access.UserContext;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Obligation implements Cloneable, Serializable {

    private UserContext author;
    private String id;
    private List<Rule> rules;

    public Obligation() {
    }

    public Obligation(UserContext author, String id) {
        this.author = author;
        this.id = id;
        this.rules = new ArrayList<>();
    }

    public Obligation(UserContext author, String id, List<Rule> rules) {
        this.author = author;
        this.id = id;
        this.rules = new ArrayList<>();
        for (Rule rule : rules) {
            this.rules.add(new Rule(rule));
        }
    }

    public Obligation(Obligation obligation) {
        this.id = obligation.id;
        this.rules = new ArrayList<>();
        for (Rule rule : obligation.getRules()) {
            this.rules.add(new Rule(rule));
        }
        this.author = obligation.author;
    }

    @Override
    public Obligation clone() {
        Obligation o;
        try {
            o = (Obligation) super.clone();
        } catch (CloneNotSupportedException e) {
            return new Obligation(this.author, this.id);
        }

        o.author = this.author;
        o.id = this.id;
        return o;
    }

    public Obligation addRule(String id, EventPattern eventPattern, Response response) {
        rules.add(new Rule(id, eventPattern, response));
        return this;
    }

    public void deleteRule(String id) {
        rules.removeIf(rule -> rule.getId().equals(id));
    }

    public UserContext getAuthor() {
        return author;
    }

    public void setAuthor(UserContext userCtx) {
        this.author = userCtx;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Rule> getRules() {
        return rules;
    }

    public Rule getRule(String ruleId) {
        for (Rule rule : rules) {
            if (rule.getId().equals(ruleId)) {
                return rule;
            }
        }

        return null;
    }

    public void setRules(List<Rule> rules) {
        this.rules = rules;
    }

    public Obligation addRule(Rule rule) {
        this.rules.add(rule);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Obligation that = (Obligation) o;
        return Objects.equals(author, that.author) && Objects.equals(id, that.id) && Objects.equals(rules, that.rules);
    }

    @Override
    public int hashCode() {
        return Objects.hash(author, id, rules);
    }
}