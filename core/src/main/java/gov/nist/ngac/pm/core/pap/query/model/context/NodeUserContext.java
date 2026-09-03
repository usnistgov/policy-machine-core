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
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Represents a user context backed by a single node (by id or name).
 */
public class NodeUserContext extends UserContext {

    /**
     * Builds a user context for the node with the given id, acting as the given process.
     */
    public static NodeUserContext of(long id, String process) {
        return new NodeUserContext(process, id, null);
    }

    /**
     * Builds a user context for the node with the given id.
     */
    public static NodeUserContext of(long id) {
        return new NodeUserContext(null, id, null);
    }

    /**
     * Builds a user context for the node with the given name, acting as the given process.
     */
    public static NodeUserContext of(String name, String process) {
        return new NodeUserContext(process, -1, name);
    }

    /**
     * Builds a user context for the node with the given name.
     */
    public static NodeUserContext of(String name) {
        return new NodeUserContext(null, -1, name);
    }

    private final long id;
    private final String name;

    private NodeUserContext(String process, long id, String name) {
        super(process);
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public Collection<Long> resolveNodeIds(NodeLookup nodeLookup) throws PMException {
        if (name == null) {
            return List.of(id);
        }

        return List.of(nodeLookup.getNodeByName(name).getId());
    }

    @Override
    public EventContextUser toEventContextUser(NodeLookup lookup) throws PMException {
        String resolvedName = name != null ? name : lookup.getNodeById(id).getName();
        return new EventContextUser(resolvedName, getProcess());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NodeUserContext that)) return false;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(getProcess(), that.getProcess());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, getProcess());
    }

    @Override
    public String toString() {
        return name != null ? "{user: " + name + "}" : "{user: " + id + "}";
    }
}
