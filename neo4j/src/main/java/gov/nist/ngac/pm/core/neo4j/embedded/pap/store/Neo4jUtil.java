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
 * licensed to NIST.
 *
 * This file is part of the policy-machine-core-neo4j module, which compiles against
 * and embeds org.neo4j:neo4j (GPLv3, Community Edition). As a combined work, this
 * module is distributed under the GNU General Public License v3.0, not the CC BY 4.0
 * license used elsewhere in this repository. See neo4j/LICENSE for the full text.
 */

// This file is part of the policy-machine-core-neo4j module, which links against
// org.neo4j:neo4j (GPLv3) and is distributed under GPLv3. See neo4j/LICENSE.
package gov.nist.ngac.pm.core.neo4j.embedded.pap.store;

import gov.nist.ngac.pm.core.common.exception.UnknownTypeException;
import gov.nist.ngac.pm.core.common.graph.node.NodeType;
import gov.nist.ngac.pm.core.common.prohibition.NodeProhibition;
import gov.nist.ngac.pm.core.common.prohibition.ProcessProhibition;
import gov.nist.ngac.pm.core.common.prohibition.Prohibition;
import gov.nist.ngac.pm.core.pap.operation.accessright.AccessRightSet;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.RelationshipType;

/**
 * Shared Neo4j labels, property keys, relationship types, and conversion helpers used across the
 * embedded Neo4j store implementations.
 */
public class Neo4jUtil {

	public static final Label NODE_LABEL = Label.label("Node");
	public static final Label PC_LABEL = Label.label("PC");
	public static final Label OA_LABEL = Label.label("OA");
	public static final Label UA_LABEL = Label.label("UA");
	public static final Label O_LABEL = Label.label("O");
	public static final Label U_LABEL = Label.label("U");
	public static final Label OBLIGATION_LABEL = Label.label("Obligation");
	public static final Label RESOURCE_ARS_LABEL = Label.label("ResourceAccessRights");
	public static final Label OPERATION_LABEL = Label.label("Operation");
	public static final Label PROHIBITION_LABEL = Label.label("Prohibition");
	public static final Label PROCESS_LABEL = Label.label("Process");

	public static final String ARSET_PROPERTY = "arset";
	public static final String INCLUSION_SET_PROPERTY = "inclusion_set";
	public static final String EXCLUSION_SET_PROPERTY = "exclusion_set";
	public static final String NAME_PROPERTY = "name";
	public static final String NODE_ID_PROPERTY = "node_id";
	public static final String PROCESS_PROPERTY = "process";
	public static final String ID_PROPERTY = "id";
	public static final String DATA_PROPERTY = "data";
	public static final String IS_CONJUNCTIVE_PROPERTY = "is_conjunctive";
	public static final String OPERATION_KIND_PROPERTY = "operation_kind";
	public static final String PML_TEXT_PROPERTY = "pml_text";
	public static final String AUTHOR_ID_PROPERTY = "author_id";
	public static final String AUTHOR_NAME_PROPERTY = "author_name";
	public static final String AUTHOR_PROCESS_PROPERTY = "author_process";

	public static final RelationshipType ASSIGNMENT_RELATIONSHIP_TYPE = RelationshipType.withName("ASSIGNED_TO");
	public static final RelationshipType ASSOCIATION_RELATIONSHIP_TYPE = RelationshipType.withName("ASSOCIATED_WITH");
	public static final RelationshipType PROHIBITION_SUBJECT_REL_TYPE = RelationshipType.withName("prohibition_subject");
	public static final RelationshipType PROHIBITION_CONTAINER_REL_TYPE = RelationshipType.withName("prohibition_container");

	/**
	 * Resolves a Neo4j node's {@link NodeType} by matching its labels against the known type names.
	 *
	 * @param node the Neo4j node to inspect
	 * @return the matching node type
	 * @throws UnknownTypeException if none of the node's labels match a known node type
	 */
	public static NodeType getNodeType(Node node) throws UnknownTypeException {
		for (Label label : node.getLabels()) {
			String labelName = label.name();

			try {
				NodeType nodeType = NodeType.toNodeType(labelName);
				return nodeType;
			} catch (UnknownTypeException e) { }
		}

		throw new UnknownTypeException(null);
	}

	/**
	 * Reconstructs a {@link Prohibition} from a Neo4j prohibition node's properties.
	 *
	 * @param prohibitionNode the Neo4j node holding the prohibition's properties
	 * @return the reconstructed prohibition
	 */
	public static Prohibition getProhibitionFromNode(Node prohibitionNode) {
		String name = String.valueOf(prohibitionNode.getProperty(NAME_PROPERTY));
		long nodeId = (long) prohibitionNode.getProperty(NODE_ID_PROPERTY);
		String process = String.valueOf(prohibitionNode.getProperty(PROCESS_PROPERTY, ""));
		AccessRightSet accessRights = new AccessRightSet((String[]) prohibitionNode.getProperty(ARSET_PROPERTY));
		Set<Long> inclusion = toLongSet((long[]) prohibitionNode.getProperty(INCLUSION_SET_PROPERTY));
		Set<Long> exclusion = toLongSet((long[]) prohibitionNode.getProperty(EXCLUSION_SET_PROPERTY));
		boolean isConjunctive = (boolean)prohibitionNode.getProperty(IS_CONJUNCTIVE_PROPERTY);

		return process.isEmpty()
			? new NodeProhibition(name, nodeId, accessRights, inclusion, exclusion, isConjunctive)
			: new ProcessProhibition(name, nodeId, process, accessRights, inclusion, exclusion, isConjunctive);
	}

	/**
	 * Maps a {@link NodeType} to its Neo4j label. Types without a dedicated label, such as ANY, fall
	 * back to an empty label.
	 *
	 * @param type the node type to map
	 * @return the corresponding Neo4j label
	 */
	public static Label typeToLabel(NodeType type) {
		return switch (type) {
			case OA -> OA_LABEL;
			case UA -> UA_LABEL;
			case U -> U_LABEL;
			case O -> O_LABEL;
			case PC -> PC_LABEL;
			default -> Label.label("");
		};
	}

	private static Set<Long> toLongSet(long[] arr) {
		return Arrays.stream(arr).boxed().collect(Collectors.toSet());
	}

}
