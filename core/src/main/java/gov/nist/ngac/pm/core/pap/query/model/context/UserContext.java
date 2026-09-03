/*
 * This Software (Policy Machine) is being made available as a public service by the
 * National Institute of Standards and Technology (NIST), an Agency of the United
 * States Department of Commerce. This software was developed in part by employees of
 * NIST and in part by NIST contractors. Copyright in portions of this software that
 * were developed by NIST contractors has been licensed or assigned to NIST. Pursuant
 * to Title 17 United States Code Section 105, works of NIST employees are not
 * subject to copyright protection in the United States. However, NIST may hold
 * international copyright in software created by its employees and domestic
 * copyright (or licensing rights) in portions of software that were assigned or
 * licensed to NIST. To the extent that NIST holds copyright in this software, it is
 * being made available under the Creative Commons Attribution 4.0 International
 * license (CC BY 4.0). The disclaimers of the CC BY 4.0 license apply to all parts
 * of the software developed or licensed by NIST.
 *
 * ACCESS THE FULL CC BY 4.0 LICENSE HERE:
 * https://creativecommons.org/licenses/by/4.0/legalcode
 */

package gov.nist.ngac.pm.core.pap.query.model.context;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.query.NodeLookup;
import gov.nist.ngac.pm.core.pap.obligation.event.EventContextUser;
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
     * Resolve the IDs of any nodes referenced in this user context. The NodeLookup provides a lookup to get node IDs.
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
