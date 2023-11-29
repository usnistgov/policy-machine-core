package gov.nist.csd.pm.policy.model.obligation.event;


import gov.nist.csd.pm.policy.model.obligation.event.subject.Subject;
import gov.nist.csd.pm.policy.model.obligation.event.target.AnyTarget;
import gov.nist.csd.pm.policy.model.obligation.event.target.Target;

import java.io.Serializable;
import java.util.*;

public class EventPattern implements Serializable {

    private Subject subject;
    private List<String> operations;
    private Target target;

    public EventPattern() {
    }

    public EventPattern(Subject subject, Performs performs) {
        this.subject = subject;
        this.operations = Collections.unmodifiableList(Arrays.asList(performs.events()));
        this.target = new AnyTarget();
    }
    public EventPattern(Subject subject, Performs performs, Target target) {
        this.subject = subject;
        this.operations = Collections.unmodifiableList(Arrays.asList(performs.events()));
        this.target = target;
    }

    public EventPattern(EventPattern eventPattern) {
        this.subject = eventPattern.subject;
        this.operations = eventPattern.operations;
        this.target = eventPattern.target;
    }

    public Subject getSubject() {
        return subject;
    }

    public List<String> getOperations() {
        return operations;
    }

    public Target getTarget() {
        return target;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventPattern that = (EventPattern) o;
        return Objects.equals(subject, that.subject) && Objects.equals(operations, that.operations) && Objects.equals(target, that.target);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subject, operations, target);
    }
}
