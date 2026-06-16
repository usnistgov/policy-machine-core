package gov.nist.csd.pm.core.pap.query.model.context;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.query.NodeLookup;
import java.util.Collection;
import java.util.List;

/**
 * Represents a target context for a single node, identified by either id or name.
 */
public class NodeTargetContext extends TargetContext {

    public static NodeTargetContext of(long id) {
        return new NodeTargetContext(id, null);
    }

    public static NodeTargetContext of(String name) {
        return new NodeTargetContext(-1, name);
    }

    private final long id;
    private final String name;

    private NodeTargetContext(long id, String name) {
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
}
