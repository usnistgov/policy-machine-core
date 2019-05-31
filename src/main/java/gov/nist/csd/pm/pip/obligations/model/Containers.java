package gov.nist.csd.pm.pip.obligations.model;

import java.util.List;

public class Containers {
    private List<EvrNode> anyOf;
    private List<EvrNode> eachOf;

    public List<EvrNode> getAnyOf() {
        return anyOf;
    }

    public void setAnyOf(List<EvrNode> anyOf) {
        this.anyOf = anyOf;
    }

    public List<EvrNode> getEachOf() {
        return eachOf;
    }

    public void setEachOf(List<EvrNode> eachOf) {
        this.eachOf = eachOf;
    }
}
