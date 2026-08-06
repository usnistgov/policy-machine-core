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
