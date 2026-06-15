package gov.nist.csd.pm.core.pap.query.model.context;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.node.NodeLookup;
import java.io.Serializable;
import java.util.Collection;

/**
 * Represents the target resource in access decisions.
 */
public abstract class TargetContext implements Serializable {

    public abstract Collection<Long> resolveNodeIds(NodeLookup nodeLookup) throws PMException;

}
