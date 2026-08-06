package gov.nist.ngac.pm.core.pap.query.model.subgraph;

import gov.nist.ngac.pm.core.common.graph.node.Node;
import java.util.Collection;

/**
 * A node paired with its descendant (or ascendant) subgraph, recursively.
 */
public record Subgraph(Node node, Collection<Subgraph> subgraphs) {
}
