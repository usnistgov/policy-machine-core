package gov.nist.ngac.pm.core.pap.graph.dag;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.query.NodeLookup;
import gov.nist.ngac.pm.core.pap.query.model.context.TargetContext;
import gov.nist.ngac.pm.core.pap.query.model.context.UserContext;

/**
 * Traverses a DAG from a starting node, calling a {@link Visitor} for each node visited.
 */
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

    /**
     * Sets the source of adjacent nodes to traverse.
     */
    public GraphWalker withAdjacencyRetriever(AdjacencyRetriever adjacencyRetriever) {
        this.adjacencyRetriever = adjacencyRetriever;
        return this;
    }

    /**
     * Sets the callback invoked per visited node; a null visitor is replaced with a no-op.
     */
    public GraphWalker withVisitor(Visitor visitor) {
        this.visitor = visitor == null ? new NoopVisitor() : visitor;
        return this;
    }

    /**
     * Sets the callback used to carry information from a visited child back to its parent; a null
     * propagator is replaced with a no-op.
     */
    public GraphWalker withPropagator(Propagator propagator) {
        this.propagator = propagator == null ? new NoopPropagator() : propagator;
        return this;
    }

    /**
     * Sets the short circuit that halts the entire walk once triggered on any node.
     */
    public GraphWalker withAllPathShortCircuit(ShortCircuit shortCircuit) {
        this.allPathsShortCircuit = shortCircuit;
        return this;
    }

    /**
     * Sets the short circuit that halts only the current path once triggered.
     */
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
     * Traverses the graph starting at the given node.
     *
     * @param start the node to start traversing at
     * @throws PMException if traversal fails
     */
    public abstract void walk(long start) throws PMException;

    /**
     * Resolves the user context to its underlying node ids and walks from each of them.
     *
     * @param userContext the user context to resolve
     * @param nodeLookup used to resolve the context to node ids
     * @throws PMException if resolving the context or walking fails
     */
    public void walk(UserContext userContext, NodeLookup nodeLookup) throws PMException {
        for (long id : userContext.resolveNodeIds(nodeLookup)) {
            walk(id);
        }
    }

    /**
     * Resolves the target context to its underlying node ids and walks from each of them.
     *
     * @param targetContext the target context to resolve
     * @param nodeLookup used to resolve the context to node ids
     * @throws PMException if resolving the context or walking fails
     */
    public void walk(TargetContext targetContext, NodeLookup nodeLookup) throws PMException {
        for (long id : targetContext.resolveNodeIds(nodeLookup)) {
            walk(id);
        }
    }

}
