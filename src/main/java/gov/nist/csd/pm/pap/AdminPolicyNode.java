package gov.nist.csd.pm.pap;

import static gov.nist.csd.pm.pap.AdminPolicy.policyClassTargetName;

public enum AdminPolicyNode {
    ADMIN_POLICY("PM_ADMIN"),
    ADMIN_POLICY_TARGET(policyClassTargetName(ADMIN_POLICY.value)),
    POLICY_CLASSES_OA("PM_ADMIN:POLICY_CLASSES"),
    PML_FUNCTIONS_TARGET("PM_ADMIN:FUNCTIONS"),
    PML_CONSTANTS_TARGET("PM_ADMIN:CONSTANTS"),
    OBLIGATIONS_TARGET("PM_ADMIN:OBLIGATIONS"),
    PROHIBITIONS_TARGET("PM_ADMIN:PROHIBITIONS");

    private final String value;

    AdminPolicyNode(String value) {
        this.value = value;
    }

    public static AdminPolicyNode fromNodeName(String child) {
        switch (child) {
            case "PM_ADMIN" -> {
                return ADMIN_POLICY;
            }
            case "PM_ADMIN:target" -> {
                return ADMIN_POLICY_TARGET;
            }
            case "PM_ADMIN:POLICY_CLASSES" -> {
                return POLICY_CLASSES_OA;
            }
            case "PM_ADMIN:FUNCTIONS" -> {
                return PML_FUNCTIONS_TARGET;
            }
            case "PM_ADMIN:CONSTANTS" -> {
                return PML_CONSTANTS_TARGET;
            }
            case "PM_ADMIN:OBLIGATIONS" -> {
                return OBLIGATIONS_TARGET;
            }
            case "PM_ADMIN:PROHIBITIONS" -> {
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