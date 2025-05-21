package gov.nist.csd.pm.pap.query.model.subgraph;

import gov.nist.csd.pm.common.graph.node.Node;

import java.util.Collection;

public record Subgraph(Node node, Collection<Subgraph> subgraphs) {
}
