package gov.nist.csd.pm.pap.admin;

public enum AdminPolicyNode {

    // Admin policy class
    ADMIN_POLICY("PM_ADMIN"),
    // Admin policy object attribute
    ADMIN_POLICY_OBJECT("PM_ADMIN:object");

    private final String value;

    AdminPolicyNode(String value) {
        this.value = value;
    }

    public static AdminPolicyNode fromNodeName(String ascendant) {
        switch (ascendant) {
            case "PM_ADMIN" -> {
                return ADMIN_POLICY;
            }
            case "PM_ADMIN:object" -> {
                return ADMIN_POLICY_OBJECT;
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
