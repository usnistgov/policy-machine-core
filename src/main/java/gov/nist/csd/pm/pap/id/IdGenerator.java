package gov.nist.csd.pm.pap.id;

import gov.nist.csd.pm.common.graph.node.NodeType;

public interface IdGenerator {

	long generateId(String name, NodeType type);

}
