/*
 * This Software (Policy Machine) is being made available as a public service by the
 * National Institute of Standards and Technology (NIST), an Agency of the United
 * States Department of Commerce. This software was developed in part by employees of
 * NIST and in part by NIST contractors. Copyright in portions of this software that
 * were developed by NIST contractors has been licensed or assigned to NIST. Pursuant
 * to Title 17 United States Code Section 105, works of NIST employees are not
 * subject to copyright protection in the United States. However, NIST may hold
 * international copyright in software created by its employees and domestic
 * copyright (or licensing rights) in portions of software that were assigned or
 * licensed to NIST. To the extent that NIST holds copyright in this software, it is
 * being made available under the Creative Commons Attribution 4.0 International
 * license (CC BY 4.0). The disclaimers of the CC BY 4.0 license apply to all parts
 * of the software developed or licensed by NIST.
 *
 * ACCESS THE FULL CC BY 4.0 LICENSE HERE:
 * https://creativecommons.org/licenses/by/4.0/legalcode
 */

package gov.nist.ngac.pm.core.common.graph.node;

import gov.nist.ngac.pm.core.common.exception.UnknownTypeException;

/**
 * Allowed types of nodes in an NGAC Graph
 * <p>
 * OA = Object Attribute
 * UA = user attribute
 * U = User
 * O = Object
 * PC = policy class
 */
public enum NodeType {
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