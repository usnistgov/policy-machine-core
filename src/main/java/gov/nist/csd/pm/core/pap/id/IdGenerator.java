package gov.nist.csd.pm.core.pap.id;

import gov.nist.csd.pm.core.common.graph.node.NodeType;

public interface IdGenerator {

	long generateId(String name, NodeType type);

}
