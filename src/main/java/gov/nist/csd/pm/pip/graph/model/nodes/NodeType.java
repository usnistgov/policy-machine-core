package gov.nist.csd.pm.pip.graph.model.nodes;

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

    private String label;

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
    public static NodeType toNodeType(String type) {
        if (type == null) {
            return null;
        }
        switch (type.toUpperCase()) {
            case "OA":
                return NodeType.OA;
            case "UA":
                return NodeType.UA;
            case "U":
                return NodeType.U;
            case "O":
                return NodeType.O;
            case "PC":
                return NodeType.PC;
            default:
                return null;
        }
    }
}