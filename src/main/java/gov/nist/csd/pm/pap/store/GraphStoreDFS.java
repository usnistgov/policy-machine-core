package gov.nist.csd.pm.pap.store;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.graph.dag.DepthFirstGraphWalker;
import gov.nist.csd.pm.common.graph.dag.Direction;

public class GraphStoreDFS extends DepthFirstGraphWalker {

    private GraphStore graphStore;

    public GraphStoreDFS(GraphStore graphStore) {
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
