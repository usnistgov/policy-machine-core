package gov.nist.csd.pm.prohibitions.model;

import gov.nist.csd.pm.exceptions.PMProhibitionException;
import gov.nist.csd.pm.graph.model.nodes.NodeContext;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

/**
 * Object representing a Prohibition.
 */
public class Prohibition  implements Serializable {
    /**
     * The name of the prohibition.
     */
    private String                    name;

    /**
     * The subject of the prohibition.
     */
    private Subject subject;

    /**
     * The list of nodes that the prohibition is applied to.
     */
    private List<NodeContext> nodes;

    /**
     * The set of operations being prohibited.
     */
    private HashSet<String>           operations;

    /**
     * Whether this prohibition is applied to the intersection of all the nodes or not.
     */
    private boolean                   intersection;

    public Prohibition(){
        this.nodes = new ArrayList<>();
    }

    public Prohibition(String name, Subject subject, List<NodeContext> nodes, HashSet<String> operations, boolean intersection) {
        if(subject == null) {
            throw new IllegalArgumentException("Prohibition subject cannot be null");
        }
        this.subject = subject;
        if(nodes == null){
            this.nodes = new ArrayList<>();
        } else {
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

    public List<NodeContext> getNodes() {
        return nodes;
    }

    public void addNode(NodeContext node){
        nodes.add(node);
    }

    public void removeNode(long id){
        for(NodeContext n : nodes){
            if(n.getID() == id){
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

    public HashSet<String> getOperations() {
        return operations;
    }

    public void setOperations(HashSet<String> operations) {
        this.operations = operations;
    }

    public boolean isIntersection() {
        return intersection;
    }

    public void setIntersection(boolean intersection) {
        this.intersection = intersection;
    }

    public boolean equals(Object o) {
        if(!(o instanceof Prohibition)) {
            return false;
        }

        Prohibition p = (Prohibition)o;
        return this.getName().equals(p.getName());
    }

    public int hashCode() {
        return Objects.hash(name);
    }

    public static class Subject {
        long subjectID;
        SubjectType subjectType;

        public Subject(long subjectID, SubjectType subjectType) {
            this.subjectID = subjectID;
            this.subjectType = subjectType;
        }

        public Subject(long subjectID, String subjectType) throws PMProhibitionException {
            this.subjectID = subjectID;
            this.subjectType = SubjectType.toType(subjectType);
        }

        public long getSubjectID() {
            return subjectID;
        }

        public SubjectType getSubjectType() {
            return subjectType;
        }
    }

    public static enum SubjectType {
        USER_ATTRIBUTE,
        USER,
        PROCESS;

        public static SubjectType toType(String subjectType) throws PMProhibitionException {
            if(subjectType == null){
                throw new PMProhibitionException("null is an invalid Prohibition subject type");
            }
            switch (subjectType.toUpperCase()){
                case "USER_ATTRIBUTE":
                    return USER_ATTRIBUTE;
                case "USER":
                    return USER;
                case "PROCESS":
                    return PROCESS;
                default:
                    throw new PMProhibitionException(String.format("%s is an invalid Prohibition subject type", subjectType));
            }
        }
    }
}
