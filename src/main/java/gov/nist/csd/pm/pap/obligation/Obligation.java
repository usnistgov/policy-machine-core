package gov.nist.csd.pm.pap.obligation;

import gov.nist.csd.pm.pap.pml.statement.operation.CreateObligationStatement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Obligation implements Serializable {

    private String author;
    private String name;
    private List<Rule> rules;

    public Obligation() {
    }

    public Obligation(String author, String name) {
        this.author = author;
        this.name = name;
        this.rules = new ArrayList<>();
    }

    public Obligation(String author, String name, List<Rule> rules) {
        this.author = author;
        this.name = name;
        this.rules = rules;
    }

    public Obligation addRule(String name, EventPattern eventPattern, Response response) {
        rules.add(new Rule(name, eventPattern, response));
        return this;
    }

    public void deleteRule(String name) {
        rules.removeIf(rule -> rule.getName().equals(name));
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String userCtx) {
        this.author = userCtx;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Rule> getRules() {
        return rules;
    }

    public Rule getRule(String ruleName) {
        for (Rule rule : rules) {
            if (rule.getName().equals(ruleName)) {
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
        return Objects.equals(author, that.author) && Objects.equals(name, that.name) && Objects.equals(rules, that.rules);
    }

    @Override
    public int hashCode() {
        return Objects.hash(author, name, rules);
    }

    @Override
    public String toString() {
        return CreateObligationStatement.fromObligation(this).toString();
    }
}