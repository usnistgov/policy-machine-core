package gov.nist.csd.pm.pip.obligations.model;

import java.util.List;

public class Target {
    private List<EvrNode> policyElements;
    private List<EvrNode> containers;

    public List<EvrNode> getPolicyElements() {
        return policyElements;
    }

    public void setPolicyElements(List<EvrNode> policyElements) {
        this.policyElements = policyElements;
    }

    public List<EvrNode> getContainers() {
        return containers;
    }

    public void setContainers(List<EvrNode> containers) {
        this.containers = containers;
    }
}
