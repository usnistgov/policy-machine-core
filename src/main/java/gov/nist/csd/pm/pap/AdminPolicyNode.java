package gov.nist.csd.pm.pap;

public enum AdminPolicyNode {
    ADMIN_POLICY("pm_admin:policy"),
    ADMIN_POLICY_TARGET("pm_admin:policy:target"),
    POLICY_CLASSES_OA("pm_admin:POLICY_CLASSES"),
    PML_FUNCTIONS_TARGET("pm_admin:FUNCTIONS"),
    PML_CONSTANTS_TARGET("pm_admin:CONSTANTS"),
    OBLIGATIONS_TARGET("pm_admin:OBLIGATIONS"),
    PROHIBITIONS_TARGET("pm_admin:PROHIBITIONS");

    private final String value;

    AdminPolicyNode(String value) {
        this.value = value;
    }

    public static AdminPolicyNode fromNodeName(String child) {
        switch (child) {
            case "pm_admin:policy" -> {
                return ADMIN_POLICY;
            }
            case "pm_admin:policy:target" -> {
                return ADMIN_POLICY_TARGET;
            }
            case "pm_admin:POLICY_CLASSES" -> {
                return POLICY_CLASSES_OA;
            }
            case "pm_admin:FUNCTIONS" -> {
                return PML_FUNCTIONS_TARGET;
            }
            case "pm_admin:CONSTANTS" -> {
                return PML_CONSTANTS_TARGET;
            }
            case "pm_admin:OBLIGATIONS" -> {
                return OBLIGATIONS_TARGET;
            }
            case "pm_admin:PROHIBITIONS" -> {
                return PROHIBITIONS_TARGET;
            }
        }

        throw new IllegalArgumentException("unknown admin policy node " + child);
    }

    public String constantName() {
        return name();
    }

    public String nodeName() {
        return value;
    }
}
