package gov.nist.csd.pm.pip.obligations.model;

import java.util.ArrayList;
import java.util.List;

public class PolicyClass {
    private List<String> anyOf;
    private List<String> eachOf;

    public PolicyClass() {
        this.anyOf = new ArrayList<>();
        this.eachOf = new ArrayList<>();
    }

    public List<String> getAnyOf() {
        return anyOf;
    }

    public void setAnyOf(List<String> anyOf) {
        this.anyOf = anyOf;
    }

    public List<String> getEachOf() {
        return eachOf;
    }

    public void setEachOf(List<String> eachOf) {
        this.eachOf = eachOf;
    }
}
