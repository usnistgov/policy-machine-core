package gov.nist.csd.pm.pip.obligations.model.actions;

import gov.nist.csd.pm.pip.obligations.model.EvrNode;
import gov.nist.csd.pm.pip.obligations.model.functions.Function;

import java.util.List;
import java.util.Map;

public class DenyAction extends Action {
    private String label;
    private EvrNode      subject;
    private List<String> operations;
    private Target       target;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public EvrNode getSubject() {
        return subject;
    }

    public void setSubject(EvrNode subject) {
        this.subject = subject;
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

    public static class Target {
        private boolean         complement;
        private boolean         intersection;
        private List<Container> containers;

        public boolean isComplement() {
            return complement;
        }

        public void setComplement(boolean complement) {
            this.complement = complement;
        }

        public boolean isIntersection() {
            return intersection;
        }

        public void setIntersection(boolean intersection) {
            this.intersection = intersection;
        }

        public List<Container> getContainers() {
            return containers;
        }

        public void setContainers(List<Container> containers) {
            this.containers = containers;
        }

        public static class Container {
            private String              name;
            private String              type;
            private Map<String, String> properties;
            private Function            function;
            private boolean             complement;

            public Container(Function function) {
                this.function = function;
            }

            public Container(String name, String type, Map<String, String> properties) {
                this.name = name;
                this.type = type;
                this.properties = properties;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public Map<String, String> getProperties() {
                return properties;
            }

            public void setProperties(Map<String, String> properties) {
                this.properties = properties;
            }

            public Function getFunction() {
                return function;
            }

            public void setFunction(Function function) {
                this.function = function;
            }

            public boolean isComplement() {
                return complement;
            }

            public void setComplement(boolean complement) {
                this.complement = complement;
            }
        }
    }
}
