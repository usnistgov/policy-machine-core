package gov.nist.csd.pm.pip.graph.model.relationships;

import gov.nist.csd.pm.exceptions.PIPException;
import gov.nist.csd.pm.pip.graph.model.nodes.NodeType;

import java.io.Serializable;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

import static gov.nist.csd.pm.pip.graph.model.nodes.NodeType.*;

/**
 * This object represents an Assignment in a NGAC graph
 */
public class Assignment extends Relationship implements Serializable {

    public Assignment(String child, String parent) {
        super(child, parent);
    }

    private static Map<NodeType, NodeType[]> validAssignments = new EnumMap<>(NodeType.class);
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
     * @throws PIPException if the child type is not allowed to be assigned to the parent type.
     */
    public static void checkAssignment(NodeType childType, NodeType parentType) throws PIPException {
        NodeType[] check = validAssignments.get(childType);
        for (NodeType nt : check) {
            if (nt.equals(parentType)) {
                return;
            }
        }

        throw new PIPException(String.format("cannot assign a node of type %s to a node of type %s", childType, parentType));
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Assignment)) {
            return false;
        }

        Assignment assignment = (Assignment) o;
        return this.source.equals(assignment.source) &&
                this.target.equals(assignment.target);
    }

    @Override
    public int hashCode() {
        return Objects.hash(source, target);
    }
}
