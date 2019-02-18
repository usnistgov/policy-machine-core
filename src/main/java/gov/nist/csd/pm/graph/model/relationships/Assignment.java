package gov.nist.csd.pm.graph.model.relationships;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.graph.model.nodes.NodeType;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Objects;

import static gov.nist.csd.pm.graph.model.nodes.NodeType.*;

/**
 * This object represents an Assignment in a NGAC graph
 */
public class Assignment extends Relationship implements Serializable {

    public Assignment(long childID, long parentID) {
        super(childID, parentID);
    }
    
    private static HashMap<NodeType, NodeType[]> validAssignments = new HashMap<>();
    {
        validAssignments.put(PC, new NodeType[]{});
        validAssignments.put(OA, new NodeType[]{PC, OA});
        validAssignments.put(O, new NodeType[]{OA});
        validAssignments.put(UA, new NodeType[]{UA, PC});
        validAssignments.put(U, new NodeType[]{UA});
    }
    /**
     * Check if the assignment provided, is valid under NGAC.
     * @param childType The type of the child.
     * @param parentType The type of the parent.
     * @throws PMException if the child type is not allowed to be assigned to the parent type.
     */
    public static void checkAssignment(NodeType childType, NodeType parentType) throws PMException {
        NodeType[] check = validAssignments.get(childType);
        for(NodeType nt : check) {
            if(nt.equals(parentType)) {
                return;
            }
        }

        throw new PMException(String.format("cannot assign a node of type %s to a node of type %s", childType, parentType));
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof Assignment)) {
            return false;
        }

        Assignment assignment = (Assignment)o;
        return this.sourceID == assignment.sourceID &&
                this.targetID == assignment.targetID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourceID, targetID);
    }
}
