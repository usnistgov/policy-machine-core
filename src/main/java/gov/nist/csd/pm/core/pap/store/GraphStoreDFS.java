package gov.nist.csd.pm.core.pap.store;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.dag.Direction;
import gov.nist.csd.pm.core.pap.graph.dag.DepthFirstGraphWalker;
import java.util.Collection;

public class GraphStoreDFS extends DepthFirstGraphWalker {

    private final GraphStore graphStore;

    public GraphStoreDFS(GraphStore graphStore) {
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
