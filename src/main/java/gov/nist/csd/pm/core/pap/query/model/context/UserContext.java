package gov.nist.csd.pm.core.pap.query.model.context;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.query.NodeLookup;
import gov.nist.csd.pm.core.pap.obligation.event.EventContextUser;
import java.io.Serializable;
import java.util.Collection;

/**
 * Represents the user in access decisions.
 */
public abstract class UserContext implements Serializable {

    private final String process;

    protected UserContext(String process) {
        this.process = process;
    }

    /**
     * Returns the process identifier associated with this user context.
     * @return the process identifier, or an empty string if none is set.
     */
    public String getProcess() {
        return process;
    }

    /**
     * Resolve the IDs of any nodes referenced in this user context. The NodeLookup provides a look-up to get node IDs.
     * @param nodeLookup A NodeLookup implementation used to lookup nodes in the policy store.
     * @return A list of the IDs.
     * @throws PMException if there is an exception resolving a node id.
     */
    public abstract Collection<Long> resolveNodeIds(NodeLookup nodeLookup) throws PMException;

    /**
     * Convert this user context to an EventContextUser for use in obligation event processing.
     *
     * @param lookup A NodeLookup to get the node names.
     * @return the EventContextUser representation of this context.
     * @throws PMException if a node lookup fails.
     */
    public abstract EventContextUser toEventContextUser(NodeLookup lookup) throws PMException;

}
