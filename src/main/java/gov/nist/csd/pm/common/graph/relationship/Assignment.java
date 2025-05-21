package gov.nist.csd.pm.common.graph.relationship;

import gov.nist.csd.pm.common.graph.node.NodeType;

import java.io.Serializable;
import java.util.EnumMap;
import java.util.Map;

/**
 * This object represents an Assignment in a NGAC graph
 */
public class Assignment extends Relationship implements Serializable {

    public Assignment(long source, long target) {
        super(source, target);
    }

    private static final Map<NodeType, NodeType[]> validAssignments = new EnumMap<>(NodeType.class);
    static {
        validAssignments.put(NodeType.PC, new NodeType[]{});
        validAssignments.put(NodeType.OA, new NodeType[]{NodeType.PC, NodeType.OA});
        validAssignments.put(NodeType.O, new NodeType[]{NodeType.OA, NodeType.PC});
        validAssignments.put(NodeType.UA, new NodeType[]{NodeType.UA, NodeType.PC});
        validAssignments.put(NodeType.U, new NodeType[]{NodeType.UA});
    }

    /**
     * Check if the assignment provided, is valid under NGAC.
     *
     * @param ascType The type of the ascendant node.
     * @param dscType The type of the descendant node.
     * @throws InvalidAssignmentException if the ascendant type is not allowed to be assigned to the descendant type.
     */
    public static void checkAssignment(NodeType ascType, NodeType dscType) throws InvalidAssignmentException {
        NodeType[] check = validAssignments.get(ascType);
        for (NodeType nt : check) {
            if (nt.equals(dscType)) {
                return;
            }
        }

        throw new InvalidAssignmentException(String.format("cannot assign a node of type %s to a node of type %s",
                ascType,
                dscType
        ));
    }
}
