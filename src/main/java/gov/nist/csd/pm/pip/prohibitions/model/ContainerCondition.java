package gov.nist.csd.pm.pip.prohibitions.model;

public class ContainerCondition {
    private String    name;
    private boolean complement;

    ContainerCondition(String name, boolean complement) {
        this.name = name;
        this.complement = complement;
    }

    public String getName() {
        return name;
    }

    public boolean isComplement() {
        return complement;
    }

    public boolean equals(Object o) {
        if (!(o instanceof ContainerCondition)) {
            return false;
        }

        ContainerCondition cc = (ContainerCondition) o;
        return this.getName().equals(cc.getName());
    }

    public int hashCode() {
        return name.hashCode();
    }
}
