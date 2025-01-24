package gov.nist.csd.pm.pap.query.model.explain;

import gov.nist.csd.pm.common.graph.node.Node;

import java.util.List;

public record ExplainNode(Node node, List<ExplainAssociation> associations) {

}
