package gov.nist.csd.pm.policy.model.graph.nodes;

import gov.nist.csd.pm.policy.exceptions.UnknownTypeException;

import java.io.Serializable;

/**
 * Allowed types of nodes in an NGAC Graph
 * <p>
 * OA = Object Attribute
 * UA = user attribute
 * U = User
 * O = Object
 * PC = policy class
 * OS = Operation Set
 */
public enum NodeType implements Serializable {
    OA("OA"),
    UA("UA"),
    U("U"),
    O("O"),
    PC("PC");

    private final String label;

    NodeType(String label) {
        this.label = label;
    }

    public String toString() {
        return label;
    }

    /**
     * Given a string, return the matching NodeType. If the type is null or not one of the types listed above,
     * null will be returned
     *
     * @param type The String type to convert to a NodeType.
     * @return the equivalent NodeType of the given String, or null if an invalid type or null is passed.
     */
    public static NodeType toNodeType(String type) throws UnknownTypeException {
        if (type == null) {
            throw new UnknownTypeException(null);
        }
        return switch (type.toUpperCase()) {
            case "OA" -> NodeType.OA;
            case "UA" -> NodeType.UA;
            case "U" -> NodeType.U;
            case "O" -> NodeType.O;
            case "PC" -> NodeType.PC;
            default -> throw new UnknownTypeException(type);
        };
    }
}