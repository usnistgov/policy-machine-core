package gov.nist.csd.pm.common.prohibition;

import java.io.Serializable;
import java.util.Objects;

public class ContainerCondition implements Serializable {

    private long id;
    private boolean complement;

    public ContainerCondition() {
    }

    public ContainerCondition(long id, boolean complement) {
        this.id = id;
        this.complement = complement;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isComplement() {
        return complement;
    }

    public void setComplement(boolean complement) {
        this.complement = complement;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ContainerCondition that = (ContainerCondition) o;
        return complement == that.complement && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, complement);
    }

    @Override
    public String toString() {
        return "ContainerCondition[" +
                "node=" + id + ", " +
                "complement=" + complement + ']';
    }

}
