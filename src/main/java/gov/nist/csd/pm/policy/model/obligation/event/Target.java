package gov.nist.csd.pm.policy.model.obligation.event;

import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.review.PolicyReview;

import java.io.Serializable;
import java.util.*;

public class Target implements Serializable {

    public static Target anyContainedIn(String anyContainedIn) {
        return new Target(Type.ANY_CONTAINED_IN, Collections.singletonList(anyContainedIn));
    }

    public static Target anyOfSet(String ... set) {
        return new Target(Type.ANY_OF_SET, Arrays.asList(set));
    }

    public static Target anyPolicyElement() {
        return new Target(Type.ANY_POLICY_ELEMENT, new ArrayList<>());
    }

    public static Target policyElement(String policyElement) {
        return new Target(Type.POLICY_ELEMENT, Arrays.asList(policyElement));
    }

    private Type type;
    private List<String> policyElements;

    public Target(Type type, List<String> policyElements) {
        this.type = type;
        this.policyElements = policyElements;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public List<String> getPolicyElements() {
        return policyElements;
    }

    public void setPolicyElements(List<String> policyElements) {
        this.policyElements = policyElements;
    }

    public String anyContainedIn() {
        return policyElements.get(0);
    }

    public List<String> anyOfSet() {
        return policyElements;
    }

    public String policyElement() {
       return policyElements.get(0);
    }

    public boolean matches(String target, PolicyReview policyReviewer) throws PMException {
        switch (type) {
            case ANY_CONTAINED_IN -> {
                return policyReviewer.isContained(target, anyContainedIn());
            }
            case POLICY_ELEMENT -> {
                return policyElement().equals(target);
            }
            case ANY_POLICY_ELEMENT -> {
                return true;
            }
            case ANY_OF_SET -> {
                return anyOfSet().contains(target);
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
        Target target = (Target) o;
        return type == target.type && Objects.equals(policyElements, target.policyElements);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, policyElements);
    }

    public enum Type {
        ANY_CONTAINED_IN,
        ANY_OF_SET,
        ANY_POLICY_ELEMENT,
        POLICY_ELEMENT
    }

}
