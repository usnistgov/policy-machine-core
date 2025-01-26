package gov.nist.csd.pm.pap.store;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.graph.dag.BreadthFirstGraphWalker;
import gov.nist.csd.pm.common.graph.dag.Direction;

public class GraphStoreBFS extends BreadthFirstGraphWalker {

    private GraphStore graphStore;

    public GraphStoreBFS(GraphStore graphStore) {
        super(null);
        this.graphStore = graphStore;
    }

    @Override
    protected long[] getNextLevel(long node) throws PMException {
        if (getDirection() == Direction.DESCENDANTS) {
            return graphStore.getAdjacentDescendants(node);
        } else {
            return graphStore.getAdjacentAscendants(node);
        }
    }
}
