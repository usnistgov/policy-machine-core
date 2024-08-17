package gov.nist.csd.pm.pap.store;

import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.pap.graph.dag.DepthFirstGraphWalker;
import gov.nist.csd.pm.pap.graph.dag.Direction;

import java.util.Collection;

public class GraphStoreDFS extends DepthFirstGraphWalker {

    private GraphStore graphStore;

    public GraphStoreDFS(GraphStore graphStore) {
        super(null);
        this.graphStore = graphStore;
    }

    @Override
    protected Collection<String> getNextLevel(String node) throws PMException {
        if (getDirection() == Direction.DESCENDANTS) {
            return graphStore.getAdjacentDescendants(node);
        } else {
            return graphStore.getAdjacentAscendants(node);
        }
    }

}
