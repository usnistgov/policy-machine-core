package gov.nist.csd.pm.core.pap.store;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.dag.Direction;
import gov.nist.csd.pm.core.pap.dag.BreadthFirstGraphWalker;
import java.util.Collection;

public class GraphStoreBFS extends BreadthFirstGraphWalker {

    private final GraphStore graphStore;

    public GraphStoreBFS(GraphStore graphStore) {
        super(null);
        this.graphStore = graphStore;
    }

    @Override
    protected Collection<Long> getNextLevel(long node) throws PMException {
        if (getDirection() == Direction.DESCENDANTS) {
            return graphStore.getAdjacentDescendants(node);
        } else {
            return graphStore.getAdjacentAscendants(node);
        }
    }
}
