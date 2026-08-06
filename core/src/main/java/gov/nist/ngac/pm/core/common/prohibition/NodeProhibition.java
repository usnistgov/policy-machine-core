package gov.nist.ngac.pm.core.common.prohibition;

import gov.nist.ngac.pm.core.pap.operation.accessright.AccessRightSet;
import java.util.Objects;
import java.util.Set;

/**
 * A {@link Prohibition} on a fixed node, denying its access rights to the containing user or user
 * attribute's associated subjects.
 */
public final class NodeProhibition extends Prohibition {

    private long nodeId;

    public NodeProhibition(String name,
                           long nodeId,
                           AccessRightSet accessRightSet,
                           Set<Long> inclusionSet,
                           Set<Long> exclusionSet,
                           boolean isConjunctive) {
        super(name, accessRightSet, inclusionSet, exclusionSet, isConjunctive);
        this.nodeId = nodeId;
    }

    public long getNodeId() {
        return nodeId;
    }

    public void setNodeId(long nodeId) {
        this.nodeId = nodeId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        NodeProhibition that = (NodeProhibition) o;
        return nodeId == that.nodeId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), nodeId);
    }
}
