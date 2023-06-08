package gov.nist.csd.pm.pap.pml.pattern;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.exception.NodeDoesNotExistException;
import gov.nist.csd.pm.pap.pml.statement.PMLStatementSerializable;
import gov.nist.csd.pm.pap.store.GraphStore;

import java.io.Serializable;
import java.util.Collection;

public abstract class Pattern implements Serializable, PMLStatementSerializable {

    public abstract boolean matches(String value, PAP pap) throws PMException;
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
        for (String s : value) {
            if (matches(s, pap)) {
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
