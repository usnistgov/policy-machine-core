package gov.nist.csd.pm.core.pap.pml.pattern;

import gov.nist.csd.pm.core.common.exception.NodeDoesNotExistException;
import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.pml.statement.PMLStatementSerializable;
import gov.nist.csd.pm.core.pap.store.GraphStore;

import java.io.Serializable;
import java.util.Collection;

public abstract class Pattern implements Serializable, PMLStatementSerializable {

    /**
     * Returns true if the given value matches this pattern. If the value is null, then return false.
     * @param value The value to check against this pattern.
     * @param pap The PAP object to get policy information relevant to the value and pattern.
     * @return True if the value matches this pattern.
     */
    public final boolean matches(String value, PAP pap) throws PMException {
        if (value == null) {
            return false;
        }

        return matchesInternal(value, pap);
    }

    public abstract boolean matchesInternal(String value, PAP pap) throws PMException;
    public abstract ReferencedNodes getReferencedNodes();

    @Override
    public abstract boolean equals(Object obj);

    @Override
    public abstract int hashCode();

    @Override
    public final String toString() {
        return toFormattedString(0);
    }

    public boolean matches(Collection<String> value, PAP pap) throws PMException {
        if (value == null) {
            return false;
        }

        for (String opValue : value) {
            if (matches(opValue, pap)) {
                return true;
            }
        }

        return false;
    }

    public void checkReferencedNodesExist(GraphStore graph) throws PMException {
        ReferencedNodes ref = getReferencedNodes();
        for (String entity : ref.nodes()) {
            if (!graph.nodeExists(entity)) {
                throw new NodeDoesNotExistException(entity);
            }
        }
    }


}
