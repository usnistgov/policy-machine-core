package gov.nist.csd.pm.pip.graph.model.relationships;

import gov.nist.csd.pm.exceptions.PIPException;
import gov.nist.csd.pm.operations.OperationSet;
import gov.nist.csd.pm.pip.graph.model.nodes.NodeType;

import java.io.Serializable;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

import static gov.nist.csd.pm.pip.graph.model.nodes.NodeType.*;

/**
 * This object represents an Association in a NGAC graph. An association is a relationship between two nodes,
 * similar to an assignment, except an Association has a set of operations included.
 */
public class Association extends Relationship implements Serializable {

    private static Map<NodeType, NodeType[]> validAssociations = new EnumMap<>(NodeType.class);
    static {
        validAssociations.put(PC, new NodeType[]{});
        validAssociations.put(OA, new NodeType[]{});
        validAssociations.put(O, new NodeType[]{});
        validAssociations.put(UA, new NodeType[]{UA, OA});
        validAssociations.put(U, new NodeType[]{});
    }

    private OperationSet operations;

    public Association(String ua, String target, OperationSet operations) {
        super(ua, target);
        this.operations = operations;
    }

    public Association(String ua, String target) {
        super(ua, target);
        this.operations = new OperationSet();
    }

    public OperationSet getOperations() {
        return operations;
    }

    public void setOperations(OperationSet operations) {
        this.operations = operations;
    }

    /**
     * Check if the provided types create a valid association.
     *
     * @param uaType     the type of the source node in the association. This should always be a user Attribute,
     *                   so an InvalidAssociationException will be thrown if it's not.
     * @param targetType the type of the target node. This can be either an Object Attribute or a user attribute.
     * @throws PIPException if the provided types do not make a valid Association under NGAC
     */
    public static void checkAssociation(NodeType uaType, NodeType targetType) throws PIPException {
        NodeType[] check = validAssociations.get(uaType);
        for (NodeType nt : check) {
            if (nt.equals(targetType)) {
                return;
            }
        }

        throw new PIPException(String.format("cannot associate a node of type %s to a node of type %s", uaType, targetType));
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Association)) {
            return false;
        }

        Association association = (Association) o;
        return this.source.equals(association.source) &&
                this.target.equals(association.target);
    }

    @Override
    public int hashCode() {
        return Objects.hash(source, target);
    }
}
