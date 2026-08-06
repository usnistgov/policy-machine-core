package gov.nist.ngac.pm.core.pap.id;

import gov.nist.ngac.pm.core.common.graph.node.NodeType;

/**
 * Generates ids for newly created nodes.
 */
public interface IdGenerator {

	/**
	 * Generates an id for a new node.
	 *
	 * @param name the node's name
	 * @param type the node's type
	 * @return the generated id
	 */
	long generateId(String name, NodeType type);

}
