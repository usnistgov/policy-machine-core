package gov.nist.csd.pm.common.graph.node;

import gov.nist.csd.pm.common.exception.UnknownTypeException;

import java.io.Serializable;

/**
 * Allowed types of nodes in an NGAC Graph
 * <p>
 * OA = Object Attribute
 * UA = user attribute
 * U = User
 * O = Object
 * PC = policy class
 */
public enum NodeType implements Serializable {
    OA(0),
    UA(1),
    U(2),
    O(3),
    PC(4),
    ANY(5);

    private final int i;

    NodeType(int i) {
        this.i = i;
    }

    public String toString() {
        return switch (i) {
            case 0 -> "OA";
            case 1 -> "UA";
            case 2 -> "U";
            case 3 -> "O";
            case 4 -> "PC";
            case 5 -> "ANY";
	        default -> throw new IllegalStateException("Unexpected value: " + i);
        };
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