package gov.nist.csd.pm.pap.admin;

import java.util.*;

import static gov.nist.csd.pm.pap.admin.AdminPolicyNode.*;

public class AdminPolicy {

    public static final Set<String> ALL_NODE_NAMES = new HashSet<>(List.of(
            PM_ADMIN_PC.nodeName(),
            PM_ADMIN_OBJECT.nodeName()
    ));

    public static final Set<String> AL_NODE_CONSTANT_NAMES = new HashSet<>(List.of(
            PM_ADMIN_PC.constantName(),
            PM_ADMIN_OBJECT.constantName()
    ));

    public static boolean isAdminPolicyNodeConstantName(String name) {
        return AL_NODE_CONSTANT_NAMES.contains(name);
    }

    public static boolean isAdminPolicyNodeName(String name) {
        return ALL_NODE_NAMES.contains(name);
    }

}
