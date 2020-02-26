package gov.nist.csd.pm.pip.obligations.model.actions;

import gov.nist.csd.pm.pip.obligations.model.EvrNode;

import java.util.ArrayList;
import java.util.List;

public class GrantAction extends Action {

    private EvrNode subject;
    private List<String> operations;
    private EvrNode target;

    public GrantAction() {
        operations = new ArrayList<>();
    }

    public EvrNode getSubject() {
        return subject;
    }

    public void setSubject(EvrNode subject) {
        this.subject = subject;
    }

    public List<String> getOperations() {
        return operations;
    }

    public void setOperations(List<String> operations) {
        this.operations = operations;
    }

    public EvrNode getTarget() {
        return target;
    }

    public void setTarget(EvrNode target) {
        this.target = target;
    }
}
