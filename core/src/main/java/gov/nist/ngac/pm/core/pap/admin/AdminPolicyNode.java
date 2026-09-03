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

package gov.nist.ngac.pm.core.pap.admin;

/**
 * The fixed set of admin policy nodes every policy is bootstrapped with.
 */
public enum AdminPolicyNode {

    PM_ADMIN_PC(-1, "PM_ADMIN"),
    PM_ADMIN_BASE_OA(-2, "PM_ADMIN:base"),
    PM_ADMIN_POLICY_CLASSES(-3, "PM_ADMIN:policy_classes"),
    PM_ADMIN_OBLIGATIONS(-4, "PM_ADMIN:obligations"),
    PM_ADMIN_PROHIBITIONS(-5, "PM_ADMIN:prohibitions"),
    PM_ADMIN_OPERATIONS(-6, "PM_ADMIN:operations"),
    PM_ADMIN_ROUTINES(-7, "PM_ADMIN:routines");

    private final long id;
    private final String name;

    AdminPolicyNode(long id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Returns this constant's Java name, as distinct from its graph node name.
     *
     * @return this constant's Java name
     */
    public String constantName() {
        return name();
    }

    /**
     * Returns this node's name in the graph.
     *
     * @return the node's name
     */
    public String nodeName() {
        return name;
    }

    /**
     * Returns this node's reserved id in the graph.
     *
     * @return the node's id
     */
    public long nodeId() {
        return id;
    }

    /**
     * Checks whether the given id belongs to one of the admin policy nodes.
     *
     * @param id the node id to check
     * @return whether the id belongs to an admin policy node
     */
    public static boolean isAdminPolicyNode(long id) {
        for (AdminPolicyNode node : values()) {
            if (node.id == id) {
                return true;
            }
        }

        return false;
    }
}
