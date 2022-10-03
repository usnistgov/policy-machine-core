package gov.nist.csd.pm.policy.model.obligation.event;

import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.review.PolicyReview;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class EventSubject implements Serializable {

    public static EventSubject anyUser() {
        return new EventSubject(Type.ANY_USER, new ArrayList<>());
    }

    public static EventSubject anyUserWithAttribute(String attr) {
        return new EventSubject(Type.ANY_USER_WITH_ATTRIBUTE, Arrays.asList(attr));
    }

    public static EventSubject process(String process) {
        return new EventSubject(Type.PROCESS, Arrays.asList(process));
    }

    public static EventSubject users(String ... users) {
        return new EventSubject(Type.USERS, Arrays.asList(users));
    }

    private Type type;
    private List<String> subjects;

    private EventSubject(Type type, List<String> subjects) {
        this.type = type;
        this.subjects = subjects;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public List<String> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<String> subjects) {
        this.subjects = subjects;
    }

    public String anyUserWithAttribute() {
        return subjects.get(0);
    }

    public String process() {
        return subjects.get(0);
    }

    public List<String> users() {
        return subjects;
    }

    public boolean matches(UserContext userCtx, PolicyReview policyReviewer) throws PMException {
        switch (type) {
            case ANY_USER -> {
                return true;
            }
            case ANY_USER_WITH_ATTRIBUTE -> {
                String user = userCtx.getUser();
                return policyReviewer.isContained(user, anyUserWithAttribute());
            }
            case PROCESS -> {
                return userCtx.getProcess().equals(process());
            }
            case USERS -> {
                return users().contains(userCtx.getUser());
            }
            default -> {
                return false;
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventSubject subject = (EventSubject) o;
        return type == subject.type && Objects.equals(subjects, subject.subjects);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, subjects);
    }

    public enum Type {
        ANY_USER,
        ANY_USER_WITH_ATTRIBUTE,
        PROCESS,
        USERS
    }
}
