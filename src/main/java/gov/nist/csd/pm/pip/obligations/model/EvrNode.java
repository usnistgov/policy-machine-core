package gov.nist.csd.pm.pip.obligations.model;

import gov.nist.csd.pm.pip.obligations.model.functions.Function;

import java.util.Map;

public class EvrNode {
    private String              name;
    private String              type;
    private Map<String, String> properties;
    private Function            function;
    private EvrProcess          process;

    public EvrNode(Function function) {
        this.function = function;
    }

    public EvrNode(String name, String type, Map<String, String> properties) {
        this.name = name;
        this.type = type;
        this.properties = properties;
    }

    public EvrNode(EvrProcess process) {
        this.process = process;
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

    public EvrProcess getProcess() {
        return process;
    }

    public void setProcess(EvrProcess process) {
        this.process = process;
    }

    public boolean equals(Object o) {
        if(!(o instanceof EvrNode)) {
            return false;
        }

        EvrNode n = (EvrNode)o;

        return this.name.equals(n.getName());
    }
}
