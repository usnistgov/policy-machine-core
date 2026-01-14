package gov.nist.csd.pm.core.impl.neo4j.embedded.pap.store;

import gov.nist.csd.pm.core.common.exception.InvalidProhibitionSubjectException;
import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.exception.UnknownTypeException;
import gov.nist.csd.pm.core.common.graph.node.NodeType;
import gov.nist.csd.pm.core.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.core.common.prohibition.ContainerCondition;
import gov.nist.csd.pm.core.common.prohibition.Prohibition;
import gov.nist.csd.pm.core.common.prohibition.ProhibitionSubject;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.neo4j.graphdb.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Neo4jUtil {

	public static final Label NODE_LABEL = Label.label("Node");
	public static final Label PC_LABEL = Label.label("PC");
	public static final Label OA_LABEL = Label.label("OA");
	public static final Label UA_LABEL = Label.label("UA");
	public static final Label O_LABEL = Label.label("O");
	public static final Label U_LABEL = Label.label("U");
	public static final Label OBLIGATION_LABEL = Label.label("Obligation");
	public static final Label RESOURCE_ARS_LABEL = Label.label("ResourceAccessRights");
	public static final Label ADMIN_OPERATION_LABEL = Label.label("AdminOp");
	public static final Label RESOURCE_OPERATION_LABEL = Label.label("ResourceOp");
	public static final Label ADMIN_ROUTINE_LABEL = Label.label("AdminRoutine");
	public static final Label PROHIBITION_LABEL = Label.label("Prohibition");
	public static final Label PROCESS_LABEL = Label.label("Process");

	public static final String ARSET_PROPERTY = "arset";
	public static final String NAME_PROPERTY = "name";
	public static final String ID_PROPERTY = "id";
	public static final String DATA_PROPERTY = "data";
	public static final String COMPLEMENT_PROPERTY = "complement";
	public static final String INTERSECTION_PROPERTY = "intersection";

	public static final RelationshipType ASSIGNMENT_RELATIONSHIP_TYPE = RelationshipType.withName("ASSIGNED_TO");
	public static final RelationshipType ASSOCIATION_RELATIONSHIP_TYPE = RelationshipType.withName("ASSOCIATED_WITH");
	public static final RelationshipType PROHIBITION_SUBJECT_REL_TYPE = RelationshipType.withName("prohibition_subject");
	public static final RelationshipType PROHIBITION_CONTAINER_REL_TYPE = RelationshipType.withName("prohibition_container");

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

	public static Prohibition getProhibitionFromNode(Node prohibitionNode) throws InvalidProhibitionSubjectException {
		String label = String.valueOf(prohibitionNode.getProperty(NAME_PROPERTY));

		// get subject
		Relationship subjectRel = prohibitionNode.getSingleRelationship(PROHIBITION_SUBJECT_REL_TYPE, Direction.INCOMING);
		Node subjectNode = subjectRel.getStartNode();
		ProhibitionSubject subject;
		if (subjectNode.hasLabel(PROCESS_LABEL)) {
			subject = new ProhibitionSubject(String.valueOf(subjectNode.getProperty(ID_PROPERTY)));
		} else {
			subject = new ProhibitionSubject((Long) subjectNode.getProperty(ID_PROPERTY));
		}

		AccessRightSet accessRights = new AccessRightSet((String[]) prohibitionNode.getProperty(ARSET_PROPERTY));

		boolean intersection = (boolean)prohibitionNode.getProperty(INTERSECTION_PROPERTY);

		List<ContainerCondition> containerConditions = new ArrayList<>();
		try(ResourceIterable<Relationship> contRels = prohibitionNode.getRelationships(Direction.INCOMING, PROHIBITION_CONTAINER_REL_TYPE)) {
			for (Relationship relationship : contRels) {
				Node contNode = relationship.getStartNode();
				containerConditions.add(new ContainerCondition(
						(Long) contNode.getProperty(ID_PROPERTY),
						Boolean.parseBoolean(relationship.getProperty(COMPLEMENT_PROPERTY).toString())
				));
			}
		}


		return new Prohibition(label, subject, accessRights, intersection, containerConditions);
	}

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

	public static String serialize(Object obj) throws PMException {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		try (ObjectOutputStream objectStream = new ObjectOutputStream(byteStream)) {
			objectStream.writeObject(obj);
		} catch (IOException e) {
			throw new PMException(e);
		}

		byte[] bytes = byteStream.toByteArray();

		return Hex.encodeHexString(bytes);
	}

	public static Object deserialize(String hex, ClassLoader classLoader) throws PMException {
		try {
			byte[] bytes = Hex.decodeHex(hex);

			ByteArrayInputStream byteStream = new ByteArrayInputStream(bytes);
			try (ObjectInputStream objectStream = new ObjectInputStream(byteStream) {
				@Override
				protected Class<?> resolveClass(ObjectStreamClass desc)
				throws IOException, ClassNotFoundException {
					return Class.forName(desc.getName(), false, classLoader);
				}
			}) {
				return objectStream.readObject();
			}
		} catch (DecoderException | ClassNotFoundException | IOException e) {
			e.printStackTrace();
			throw new PMException(e);
		}
	}

}
