package gov.nist.csd.pm.pip.obligations.model;

import java.util.List;

public class EventPattern {
    private Subject      subject;
    private PolicyClass  policyClass;
    private List<String> operations;
    private Target target;

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public PolicyClass getPolicyClass() {
        return policyClass;
    }

    public void setPolicyClass(PolicyClass policyClass) {
        this.policyClass = policyClass;
    }

    public List<String> getOperations() {
        return operations;
    }

    public void setOperations(List<String> operations) {
        this.operations = operations;
    }

    public Target getTarget() {
        return target;
    }

    public void setTarget(Target target) {
        this.target = target;
    }
}
