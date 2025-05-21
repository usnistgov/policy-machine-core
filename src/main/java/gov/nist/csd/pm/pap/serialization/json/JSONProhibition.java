package gov.nist.csd.pm.pap.serialization.json;

import gov.nist.csd.pm.common.prohibition.ContainerCondition;
import gov.nist.csd.pm.common.prohibition.ProhibitionSubject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class JSONProhibition {
    private String name;
    private JSONSubject subject;
    private Collection<ContainerCondition> containers;
    private List<String> arset;
    private Boolean intersection;

    public JSONProhibition() {
        this.containers = new ArrayList<>();
        this.arset = new ArrayList<>();
        this.intersection = true;
    }

    public JSONProhibition(String name, JSONSubject subject, Collection<ContainerCondition> containers,
                           List<String> arset, Boolean intersection) {
        this.name = name;
        this.subject = subject;
        this.containers = containers;
        this.arset = arset;
        this.intersection = intersection != null ? intersection : true;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public JSONSubject getSubject() {
        return subject;
    }

    public void setSubject(JSONSubject subject) {
        this.subject = subject;
    }

    public Collection<ContainerCondition> getContainers() {
        return containers;
    }

    public void setContainers(List<ContainerCondition> containers) {
        this.containers = containers;
    }

    public List<String> getArset() {
        return arset;
    }

    public void setArset(List<String> arset) {
        this.arset = arset;
    }

    public Boolean getIntersection() {
        return intersection;
    }

    public void setIntersection(Boolean intersection) {
        this.intersection = intersection;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JSONProhibition that)) return false;
        return Objects.equals(name, that.name) && 
               Objects.equals(subject, that.subject) && 
               Objects.equals(containers, that.containers) && 
               Objects.equals(arset, that.arset) &&
               Objects.equals(intersection, that.intersection);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, subject, containers, arset, intersection);
    }

    @Override
    public String toString() {
        return "JSONProhibition{" +
                "name='" + name + '\'' +
                ", subject=" + subject +
                ", containers=" + containers +
                ", operations=" + arset +
                ", intersection=" + intersection +
                '}';
    }
    
    // Inner class for subject
    public static class JSONSubject {

        private Long node;
        private String process;

        public JSONSubject() {
        }

        public JSONSubject(Long node) {
            this.node = node;
        }

        public JSONSubject(String process) {
            this.process = process;
        }

        public static JSONSubject fromProhibitionSubject(ProhibitionSubject subject) {
            return subject.isNode() ? new JSONSubject(subject.getNodeId()) : new JSONSubject(subject.getProcess());
        }

        public Long getNode() {
            return node;
        }

        public void setNode(Long node) {
            this.node = node;
        }

        public String getProcess() {
            return process;
        }

        public void setProcess(String process) {
            this.process = process;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (!(o instanceof JSONSubject that))
                return false;
            return Objects.equals(node, that.node) &&
                Objects.equals(process, that.process);
        }

        @Override
        public int hashCode() {
            return Objects.hash(node, process);
        }

        @Override
        public String toString() {
            if (node != null) {
                return "JSONSubject{node=" + node + '}';
            } else {
                return "JSONSubject{process='" + process + "'}";
            }
        }
    }
}
