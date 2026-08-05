package gov.nist.ngac.pm.core.pap.id;

import gov.nist.ngac.pm.core.common.graph.node.NodeType;

public interface IdGenerator {

	long generateId(String name, NodeType type);

}
