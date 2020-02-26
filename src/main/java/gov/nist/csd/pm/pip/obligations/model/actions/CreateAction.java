package gov.nist.csd.pm.pip.obligations.model.actions;

import gov.nist.csd.pm.pip.obligations.model.EvrNode;
import gov.nist.csd.pm.pip.obligations.model.Rule;

import java.util.ArrayList;
import java.util.List;

public class CreateAction extends Action {
    private List<CreateNode> createNodesList;
    private List<Rule>       rules;

    public CreateAction() {
        createNodesList = new ArrayList<>();
        rules = new ArrayList<>();
    }

    public List<CreateNode> getCreateNodesList() {
        return createNodesList;
    }

    public void setCreateNodesList(List<CreateNode> createNodesList) {
        this.createNodesList = createNodesList;
    }

    public void addCreateNode(CreateNode createNode) {
        this.createNodesList.add(createNode);
    }

    public List<Rule> getRules() {
        return rules;
    }

    public void setRules(List<Rule> rules) {
        this.rules = rules;
    }

    public void addRule(Rule rule) {
        this.rules.add(rule);
    }

    public static class CreateNode {
        EvrNode what;
        EvrNode where;

        public CreateNode(EvrNode what, EvrNode where) {
            this.what = what;
            this.where = where;
        }

        public EvrNode getWhat() {
            return what;
        }

        public void setWhat(EvrNode what) {
            this.what = what;
        }

        public EvrNode getWhere() {
            return where;
        }

        public void setWhere(EvrNode where) {
            this.where = where;
        }
    }
}
