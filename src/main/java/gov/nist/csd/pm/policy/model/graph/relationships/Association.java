package gov.nist.csd.pm.policy.model.graph.relationships;

import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.graph.nodes.NodeType;

import java.io.Serializable;
import java.util.EnumMap;
import java.util.Map;

import static gov.nist.csd.pm.policy.model.graph.nodes.NodeType.*;

/**
 * This object represents an Association in a NGAC graph. An association is a relationship between two nodes,
 * similar to an assignment, except an Association has a set of operations included.
 */
public class Association extends Relationship implements Serializable {

    public Association() {
        super();
    }

    public Association(String source, String target, AccessRightSet ars) {
        super(source, target, ars);
    }

    public Association(String ua, String target) {
        super(ua, target, new AccessRightSet());
    }

    private static final Map<NodeType, NodeType[]> validAssociations = new EnumMap<>(NodeType.class);
    static {
        validAssociations.put(PC, new NodeType[]{});
        validAssociations.put(OA, new NodeType[]{});
        validAssociations.put(O, new NodeType[]{});
        validAssociations.put(UA, new NodeType[]{UA, OA});
        validAssociations.put(U, new NodeType[]{});
    }

    /**
     * Check if the provided types create a valid association.
     *
     * @param uaType     the type of the source node in the association. This should always be a user Attribute,
     *                   so an InvalidAssociationException will be thrown if it's not.
     * @param targetType the type of the target node. This can be either an Object Attribute or a user attribute.
     * @throws InvalidAssociationException if the provided types do not make a valid Association under NGAC
     */
    public static void checkAssociation(NodeType uaType, NodeType targetType) throws InvalidAssociationException {
        NodeType[] check = validAssociations.get(uaType);
        for (NodeType nt : check) {
            if (nt.equals(targetType)) {
                return;
            }
        }

        throw new InvalidAssociationException(String.format("cannot associate a node of type %s to a node of type %s", uaType, targetType));
    }
}
