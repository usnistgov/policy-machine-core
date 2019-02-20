package gov.nist.csd.pm.prohibitions.model;

import gov.nist.csd.pm.exceptions.PMException;

import java.util.*;

/**
 * Object representing a Prohibition.
 */
public class Prohibition {

    private String          name;
    private Subject         subject;
    private List<Node>      nodes;
    private Set<String> operations;
    private boolean         intersection;

    public Prohibition() {
        this.nodes = new ArrayList<>();
    }

    public Prohibition(String name, Subject subject, List<Node> nodes, Set<String> operations, boolean intersection) {
        if (subject == null) {
            throw new IllegalArgumentException("Prohibition subject cannot be null");
        }
        this.subject = subject;
        if (nodes == null) {
            this.nodes = new ArrayList<>();
        }
        else {
            this.nodes = nodes;
        }
        this.name = name;
        this.operations = operations;
        this.intersection = intersection;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public void addNode(Node node) {
        nodes.add(node);
    }

    public void removeNode(long id) {
        for (Node n : nodes) {
            if (n.getID() == id) {
                nodes.remove(n);
                return;
            }
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<String> getOperations() {
        return operations;
    }

    public void setOperations(Set<String> operations) {
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

    public static class Subject {
        long subjectID;
        Type subjectType;

        /**
         * Prohibition Subject constructor.  The ID cannot be 0 and the type cannot be null.
         *
         * @param subjectID
         * @param subjectType
         */
        public Subject(long subjectID, Type subjectType) {
            if (subjectID == 0) {
                throw new IllegalArgumentException("a prohibition subject cannot have an ID of 0");
            }
            else if (subjectType == null) {
                throw new IllegalArgumentException("a prohibition subject cannot have a null type");
            }
            this.subjectID = subjectID;
            this.subjectType = subjectType;
        }

        public long getSubjectID() {
            return subjectID;
        }

        public Type getSubjectType() {
            return subjectType;
        }

        public enum Type {
            USER_ATTRIBUTE,
            USER,
            PROCESS;

            /**
             * Given a string, return the corresponding Type.  If the string is null, an IllegalArgumentException will
             * be thrown, and if the string does not match any of (USER, USER_ATTRIBUTE, PROCESS) a PMProhbitionExceptino will
             * be thrown because the provided string is not a valid subject type.
             *
             * @param subjectType the string to convert to a Type.
             * @return the SUbjectType tht corresponds to the given string.
             * @throws IllegalArgumentException if the given string is null.
             * @throws PMException              if the given string is not a valid subject.
             */
            public static Type toType(String subjectType) throws PMException {
                if (subjectType == null) {
                    throw new IllegalArgumentException("null is an invalid Prohibition subject type");
                }
                switch (subjectType.toUpperCase()) {
                    case "USER_ATTRIBUTE":
                        return USER_ATTRIBUTE;
                    case "USER":
                        return USER;
                    case "PROCESS":
                        return PROCESS;
                    default:
                        throw new PMException(String.format("%s is an invalid Prohibition subject type", subjectType));
                }
            }
        }
    }

    public static class Node {
        long id;
        boolean complement;

        public Node(long id, boolean complement) {
            this.id = id;
            this.complement = complement;
        }

        public long getID() {
            return id;
        }

        public boolean isComplement() {
            return complement;
        }

        public boolean equals(Object o) {
            if(!(o instanceof Node)) {
                return false;
            }

            Node node = (Node)o;
            return this.getID() == node.getID();
        }

        public int hashCode() {
            return Objects.hash(id);
        }
    }
}
