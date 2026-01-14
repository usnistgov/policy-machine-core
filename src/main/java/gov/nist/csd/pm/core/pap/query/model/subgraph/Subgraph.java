package gov.nist.csd.pm.core.pap.query.model.subgraph;

import gov.nist.csd.pm.core.common.graph.node.Node;
import java.util.Collection;

public record Subgraph(Node node, Collection<Subgraph> subgraphs) {
}
