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
