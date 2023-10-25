package gov.nist.csd.pm.policy.model.obligation.event.target;

import gov.nist.csd.pm.pdp.PolicyReviewer;
import gov.nist.csd.pm.policy.exceptions.PMException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class Target implements Serializable {

    protected List<String> targets;

    public Target(List<String> targets) {
        this.targets = targets;
    }

    public Target(String ... targets) {
        this.targets = new ArrayList<>(List.of(targets));
    }

    public abstract boolean matches(String target, PolicyReviewer policyReviewer) throws PMException;

    public List<String> getTargets() {
        return targets;
    }

    public boolean containsNode(String nodeName) {
        return targets.contains(nodeName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Target target = (Target) o;
        return Objects.equals(targets, target.targets);
    }

    @Override
    public int hashCode() {
        return Objects.hash(targets);
    }
}
