package gov.nist.csd.pm.policy.model.obligation;

import gov.nist.csd.pm.policy.model.obligation.event.EventPattern;
import gov.nist.csd.pm.policy.model.access.UserContext;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Obligation implements Cloneable, Serializable {

    private UserContext author;
    private String label;
    private List<Rule> rules;

    public Obligation() {
    }

    public Obligation(UserContext author, String label) {
        this.author = author;
        this.label = label;
        this.rules = new ArrayList<>();
    }

    public Obligation(UserContext author, String label, List<Rule> rules) {
        this.author = author;
        this.label = label;
        this.rules = new ArrayList<>();
        for (Rule rule : rules) {
            this.rules.add(new Rule(rule));
        }
    }

    public Obligation(Obligation obligation) {
        this.label = obligation.label;
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
            return new Obligation(this.author, this.label);
        }

        o.author = this.author;
        o.label = this.label;
        return o;
    }

    public Obligation addRule(String label, EventPattern eventPattern, Response response) {
        rules.add(new Rule(label, eventPattern, response));
        return this;
    }

    public void deleteRule(String label) {
        rules.removeIf(rule -> rule.getLabel().equals(label));
    }

    public UserContext getAuthor() {
        return author;
    }

    public void setAuthor(UserContext userCtx) {
        this.author = userCtx;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<Rule> getRules() {
        return rules;
    }

    public Rule getRule(String ruleLabel) {
        for (Rule rule : rules) {
            if (rule.getLabel().equals(ruleLabel)) {
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
        return Objects.equals(author, that.author) && Objects.equals(label, that.label) && Objects.equals(rules, that.rules);
    }

    @Override
    public int hashCode() {
        return Objects.hash(author, label, rules);
    }
}