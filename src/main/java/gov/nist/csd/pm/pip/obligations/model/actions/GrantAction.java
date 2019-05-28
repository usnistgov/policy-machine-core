package gov.nist.csd.pm.pip.obligations.model.actions;

import gov.nist.csd.pm.pip.obligations.model.EvrNode;

import java.util.ArrayList;
import java.util.List;

public class GrantAction extends Action {

    private List<EvrNode> subjects;
    private List<String> operations;
    private List<EvrNode> targets;

    public GrantAction() {
        subjects = new ArrayList<>();
        operations = new ArrayList<>();
        targets = new ArrayList<>();
    }

    public List<EvrNode> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<EvrNode> subjects) {
        this.subjects = subjects;
    }

    public List<String> getOperations() {
        return operations;
    }

    public void setOperations(List<String> operations) {
        this.operations = operations;
    }

    public List<EvrNode> getTargets() {
        return targets;
    }

    public void setTargets(List<EvrNode> targets) {
        this.targets = targets;
    }
}
