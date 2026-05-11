package gov.nist.csd.pm.core.pap.graph.dag;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.query.model.context.NodeLookup;
import gov.nist.csd.pm.core.pap.query.model.context.TargetContext;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;

public abstract class GraphWalker {

    protected AdjacencyRetriever adjacencyRetriever;
    protected Visitor visitor;
    protected Propagator propagator;
    protected ShortCircuit allPathsShortCircuit;
    protected ShortCircuit singlePathShortCircuit;
    
    public GraphWalker(AdjacencyRetriever adjacencyRetriever) {
        this.adjacencyRetriever = adjacencyRetriever;
        this.visitor = new NoopVisitor();
        this.propagator = new NoopPropagator();
        this.allPathsShortCircuit = new NoopShortCircuit();
        this.singlePathShortCircuit = new NoopShortCircuit();
    }

    public GraphWalker withAdjacencyRetriever(AdjacencyRetriever adjacencyRetriever) {
        this.adjacencyRetriever = adjacencyRetriever;
        return this;
    }

    public GraphWalker withVisitor(Visitor visitor) {
        this.visitor = visitor == null ? new NoopVisitor() : visitor;
        return this;
    }

    public GraphWalker withPropagator(Propagator propagator) {
        this.propagator = propagator == null ? new NoopPropagator() : propagator;
        return this;
    }

    public GraphWalker withAllPathShortCircuit(ShortCircuit shortCircuit) {
        this.allPathsShortCircuit = shortCircuit;
        return this;
    }

    public GraphWalker withSinglePathShortCircuit(ShortCircuit shortCircuit) {
        this.singlePathShortCircuit = shortCircuit;
        return this;
    }

    public AdjacencyRetriever getAdjacencyRetriever() {
        return adjacencyRetriever;
    }

    public Visitor getVisitor() {
        return visitor;
    }

    public Propagator getPropagator() {
        return propagator;
    }

    public ShortCircuit getAllPathsShortCircuit() {
        return allPathsShortCircuit;
    }

    public ShortCircuit getSinglePathShortCircuit() {
        return singlePathShortCircuit;
    }
    
    /**
     * Traverse the graph starting at the given node.
     * @param start The node to start traversing at.
     * @throws PMException If there is an exception in the PM.
     */
    public abstract void walk(long start) throws PMException;

    public void walk(UserContext userContext, NodeLookup nodeLookup) throws PMException {
        userContext.walk(this, nodeLookup);
    }

    public void walk(TargetContext targetContext, NodeLookup nodeLookup) throws PMException {
        targetContext.walk(this, nodeLookup);
    }

}
