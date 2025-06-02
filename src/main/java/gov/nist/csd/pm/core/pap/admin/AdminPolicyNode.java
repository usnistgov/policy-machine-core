package gov.nist.csd.pm.core.pap.admin;

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

    public String constantName() {
        return name();
    }

    public String nodeName() {
        return name;
    }

    public long nodeId() {
        return id;
    }

    public static boolean isAdminPolicyNode(long id) {
        for (AdminPolicyNode node : values()) {
            if (node.id == id) {
                return true;
            }
        }

        return false;
    }
}
