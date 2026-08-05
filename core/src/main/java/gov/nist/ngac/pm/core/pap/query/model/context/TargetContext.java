package gov.nist.ngac.pm.core.pap.query.model.context;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.query.NodeLookup;
import java.io.Serializable;
import java.util.Collection;

/**
 * Represents the target resource in access decisions.
 */
public abstract class TargetContext implements Serializable {

    public abstract Collection<Long> resolveNodeIds(NodeLookup nodeLookup) throws PMException;

}
