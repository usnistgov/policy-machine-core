package gov.nist.csd.pm.core.pap.query.model.context;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.query.NodeLookup;
import gov.nist.csd.pm.core.pap.obligation.event.EventContextUser;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Represents a user context backed by a single node (by id or name).
 */
public class NodeUserContext extends UserContext {

    public static NodeUserContext of(long id, String process) {
        return new NodeUserContext(process, id, null);
    }

    public static NodeUserContext of(long id) {
        return new NodeUserContext(null, id, null);
    }

    public static NodeUserContext of(String name, String process) {
        return new NodeUserContext(process, -1, name);
    }

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
