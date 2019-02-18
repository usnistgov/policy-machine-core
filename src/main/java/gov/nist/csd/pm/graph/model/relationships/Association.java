package gov.nist.csd.pm.graph.model.relationships;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.graph.model.nodes.NodeType;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;

import static gov.nist.csd.pm.graph.model.nodes.NodeType.*;

/**
 * This object represents an Association in a NGAC graph. An association is a relationship between two nodes,
 * similar to an assignment, except an Association has a set of operations included.
 */
public class Association extends Relationship implements Serializable {

    private static HashMap<NodeType, NodeType[]> validAssociations = new HashMap<>();
    {
        validAssociations.put(PC, new NodeType[]{});
        validAssociations.put(OA, new NodeType[]{});
        validAssociations.put(O, new NodeType[]{});
        validAssociations.put(UA, new NodeType[]{UA, OA});
        validAssociations.put(U, new NodeType[]{});
    }

    private HashSet<String> operations;

    public Association(long uaID, long targetID, HashSet<String> operations) {
        super(uaID, targetID);
        this.operations = operations;
    }

    public HashSet<String> getOperations() {
        return operations;
    }

    public void setOperations(HashSet<String> operations) {
        this.operations = operations;
    }

    /**
     * Check if the provided types create a valid association.
     * @param uaType the type of the source node in the association. This should always be a user Attribute,
     *               so an InvalidAssociationException will be thrown if it's not.
     * @param targetType the type of the target node. This can be either an Object Attribute or a user attribute.
     * @throws PMException if the provided types do not make a valid Association under NGAC
     */
    public static void checkAssociation(NodeType uaType, NodeType targetType) throws PMException {
        NodeType[] check = validAssociations.get(uaType);
        for(NodeType nt : check) {
            if(nt.equals(targetType)) {
                return;
            }
        }

        throw new PMException(String.format("cannot assign a node of type %s to a node of type %s", uaType, targetType));
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof Association)) {
            return false;
        }

        Association association = (Association)o;
        return this.sourceID == association.sourceID &&
                this.targetID == association.targetID &&
                this.operations.equals(association.operations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourceID, targetID, operations);
    }
}
