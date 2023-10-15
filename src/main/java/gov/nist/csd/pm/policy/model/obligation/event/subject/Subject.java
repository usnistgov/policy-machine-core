package gov.nist.csd.pm.policy.model.obligation.event.subject;

import gov.nist.csd.pm.pdp.PolicyReviewer;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class Subject implements Serializable {

    protected List<String> subjects;

    public Subject(List<String> subjects) {
        this.subjects = subjects;
    }

    public Subject(String ... subjects) {
        this.subjects = new ArrayList<>(List.of(subjects));
    }

    public abstract boolean matches(UserContext userCtx, PolicyReviewer policyReviewer) throws PMException;

    public List<String> getSubjects() {
        return subjects;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Subject that = (Subject) o;
        return Objects.equals(subjects, that.subjects);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subjects);
    }
}
