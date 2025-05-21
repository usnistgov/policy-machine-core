package gov.nist.csd.pm.pap.admin;

public enum AdminPolicyNode {

    // Admin policy class
    PM_ADMIN_PC(-1, "PM_ADMIN"),
    // Admin policy object attribute
    PM_ADMIN_OBJECT(-2, "PM_ADMIN:object");

    private final long id;
    private final String name;

    AdminPolicyNode(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static AdminPolicyNode fromNodeName(String ascendant) {
        switch (ascendant) {
            case "PM_ADMIN" -> {
                return PM_ADMIN_PC;
            }
            case "PM_ADMIN:object" -> {
                return PM_ADMIN_OBJECT;
            }
        }

        throw new IllegalArgumentException("unknown admin policy node " + ascendant);
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
}
