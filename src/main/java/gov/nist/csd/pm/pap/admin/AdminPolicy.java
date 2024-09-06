package gov.nist.csd.pm.pap.admin;

import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.pap.exception.*;

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

    /**
     * Create {@link AdminPolicyNode#PM_ADMIN_PC}
     * Create the {@link AdminPolicyNode#PM_ADMIN_PC} policy class.<p>
     * Create the {@link AdminPolicyNode#PM_ADMIN_OBJECT} in the PM_ADMIN_PC.<p>
     *
     * @param verifier The verifier used to verify the admin policy nodes exist.
     * @throws PMException If there is an error verifying any element of the admin policy.
     */
    public static void verify(Verifier verifier) throws PMException {
        try {
            verifier.verifyAdminPolicy();
        } catch (PMException e) {
            throw new AdminPolicyVerificationException(e);
        }
    }

    public interface Verifier {
        void verifyAdminPolicy() throws PMException;
    }


}
