package gov.nist.csd.pm.pip.prohibitions.model;

import gov.nist.csd.pm.operations.OperationSet;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Object representing a Prohibition.
 */
public class Prohibition {

    private String       name;
    private String      subject;
    private Map<String, Boolean>   containers;
    private OperationSet operations;
    private boolean      intersection;

    private Prohibition(String name, String subject, Map<String, Boolean> containers, OperationSet operations, boolean intersection) {
        if (subject == null) {
            throw new IllegalArgumentException("Prohibition subject cannot be null");
        }

        this.name = name;
        this.subject = subject;

        if (containers == null) {
            this.containers = new HashMap<>();
        } else {
            this.containers = containers;
        }

        if (operations == null) {
            this.operations = new OperationSet();
        } else {
            this.operations = operations;
        }

        this.intersection = intersection;
    }

    public Prohibition(Prohibition prohibition) {
        this.name = prohibition.getName();
        this.subject = prohibition.getSubject();
        this.containers = new HashMap<>();
        for (String cont : prohibition.getContainers().keySet()) {
            this.containers.put(cont, prohibition.getContainers().get(cont));
        }
        this.operations = prohibition.getOperations();
        this.intersection = prohibition.isIntersection();
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Map<String, Boolean> getContainers() {
        return containers;
    }

    public void addContainer(String name, boolean complement) {
        containers.put(name, complement);
    }

    public void removeContainerCondition(String name) {
        containers.remove(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OperationSet getOperations() {
        return operations;
    }

    public void setOperations(OperationSet operations) {
        this.operations = operations;
    }

    public boolean isIntersection() {
        return intersection;
    }

    public void setIntersection(boolean intersection) {
        this.intersection = intersection;
    }

    public boolean equals(Object o) {
        if (!(o instanceof Prohibition)) {
            return false;
        }

        Prohibition p = (Prohibition) o;
        return this.getName().equals(p.getName());
    }

    public int hashCode() {
        return Objects.hash(name);
    }

    public static class Builder {

        private String       name;
        private String      subject;
        private Map<String, Boolean>   containers;
        private OperationSet operations;
        private boolean      intersection;

        public Builder(String name, String subject, OperationSet operations) {
            this.name = name;
            this.subject = subject;
            this.containers = new HashMap<>();
            this.operations = operations;
            this.intersection = false;
        }

        public Builder addContainer(String container, boolean complement) {
            containers.put(container, complement);
            return this;
        }

        public Builder setIntersection(boolean intersection) {
            this.intersection = intersection;
            return this;
        }

        public Prohibition build() {
            return new Prohibition(name, subject, containers, operations, intersection);
        }
    }
}
