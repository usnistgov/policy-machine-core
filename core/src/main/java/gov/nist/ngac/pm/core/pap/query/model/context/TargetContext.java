package gov.nist.ngac.pm.core.pap.query.model.context;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.query.NodeLookup;
import java.io.Serializable;
import java.util.Collection;

/**
 * Represents the target resource in access decisions.
 */
public abstract class TargetContext implements Serializable {

    /**
     * Resolves this context to the concrete node ids it refers to, looking up names via the given lookup
     * if needed.
     *
     * @param nodeLookup used to resolve any node names to ids
     * @return the resolved node ids
     * @throws PMException if a name lookup fails
     */
    public abstract Collection<Long> resolveNodeIds(NodeLookup nodeLookup) throws PMException;

}
