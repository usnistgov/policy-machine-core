package gov.nist.csd.pm.policy.model.prohibition;

import java.io.Serializable;

public final class ContainerCondition implements Serializable {

    private String name;
    private boolean complement;

    public ContainerCondition() {
    }

    public ContainerCondition(String name, boolean complement) {
        this.name = name;
        this.complement = complement;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isComplement() {
        return complement;
    }

    public void setComplement(boolean complement) {
        this.complement = complement;
    }

    public boolean equals(Object o) {
        if (!(o instanceof ContainerCondition cc)) {
            return false;
        }

        return this.name.equals(cc.name) && this.complement == cc.complement;
    }

    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return "ContainerCondition[" +
                "name=" + name + ", " +
                "complement=" + complement + ']';
    }

}
