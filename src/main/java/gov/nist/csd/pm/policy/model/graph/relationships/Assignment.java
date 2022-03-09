package gov.nist.csd.pm.policy.model.graph.relationships;

import gov.nist.csd.pm.policy.model.graph.nodes.NodeType;

import java.io.Serializable;
import java.util.EnumMap;
import java.util.Map;

import static gov.nist.csd.pm.policy.model.graph.nodes.NodeType.*;

/**
 * This object represents an Assignment in a NGAC graph
 */
public class Assignment extends Relationship implements Serializable {

    public Assignment(String source, String target) {
        super(source, target);
    }

    private static final Map<NodeType, NodeType[]> validAssignments = new EnumMap<>(NodeType.class);
    static {
        validAssignments.put(PC, new NodeType[]{});
        validAssignments.put(OA, new NodeType[]{PC, OA});
        validAssignments.put(O, new NodeType[]{OA});
        validAssignments.put(UA, new NodeType[]{UA, PC});
        validAssignments.put(U, new NodeType[]{UA});
    }

    /**
     * Check if the assignment provided, is valid under NGAC.
     *
     * @param childType  The type of the child.
     * @param parentType The type of the parent.
     * @throws InvalidAssignmentException if the child type is not allowed to be assigned to the parent type.
     */
    public static void checkAssignment(NodeType childType, NodeType parentType) throws InvalidAssignmentException {
        NodeType[] check = validAssignments.get(childType);
        for (NodeType nt : check) {
            if (nt.equals(parentType)) {
                return;
            }
        }

        throw new InvalidAssignmentException(String.format("cannot assign a node of type %s to a node of type %s", childType, parentType));
    }
}
