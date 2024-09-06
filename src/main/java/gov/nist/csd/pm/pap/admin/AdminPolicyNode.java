package gov.nist.csd.pm.pap.admin;

public enum AdminPolicyNode {

    // Admin policy class
    PM_ADMIN_PC("PM_ADMIN"),
    // Admin policy object attribute
    PM_ADMIN_OBJECT("PM_ADMIN:object");

    private final String value;

    AdminPolicyNode(String value) {
        this.value = value;
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
        return value;
    }
}
