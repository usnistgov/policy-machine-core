package gov.nist.csd.pm.pip.obligations.model.actions;

import gov.nist.csd.pm.pip.obligations.model.EvrNode;

import java.util.List;

public class DeleteAction extends Action {

    private List<EvrNode> nodes;
    private AssignAction assignments;
    private List<GrantAction> associations;
    private List<String> prohibitions;
    private List<String> rules;

    public List<EvrNode> getNodes() {
        return nodes;
    }

    public void setNodes(List<EvrNode> nodes) {
        this.nodes = nodes;
    }

    public AssignAction getAssignments() {
        return assignments;
    }

    public void setAssignments(AssignAction assignments) {
        this.assignments = assignments;
    }

    public List<GrantAction> getAssociations() {
        return associations;
    }

    public void setAssociations(List<GrantAction> associations) {
        this.associations = associations;
    }

    public List<String> getProhibitions() {
        return prohibitions;
    }

    public void setProhibitions(List<String> prohibitions) {
        this.prohibitions = prohibitions;
    }

    public List<String> getRules() {
        return rules;
    }

    public void setRules(List<String> rules) {
        this.rules = rules;
    }
}
