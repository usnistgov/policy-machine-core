package gov.nist.csd.pm.policy.model.prohibition;

import java.io.Serializable;

public record ContainerCondition(String name, boolean complement) implements Serializable {

    public boolean equals(Object o) {
        if (!(o instanceof ContainerCondition cc)) {
            return false;
        }

        return this.name.equals(cc.name) && this.complement == cc.complement;
    }

    public int hashCode() {
        return name.hashCode();
    }
}
