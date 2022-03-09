package gov.nist.csd.pm.policy.model.prohibition;

public record ContainerCondition(String name, boolean complement) {

    public boolean equals(Object o) {
        if (!(o instanceof ContainerCondition cc)) {
            return false;
        }

        return this.name().equals(cc.name());
    }

    public int hashCode() {
        return name.hashCode();
    }
}
