package gov.nist.csd.pm.pap;

import gov.nist.csd.pm.policy.exceptions.*;
import gov.nist.csd.pm.policy.model.graph.relationships.InvalidAssignmentException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static gov.nist.csd.pm.pap.AdminPolicyNode.*;

public class AdminPolicy {

    public static final Set<String> ALL_NODE_NAMES = new HashSet<>(List.of(
            ADMIN_POLICY.nodeName(),
            ADMIN_POLICY_TARGET.nodeName(),
            POLICY_CLASS_TARGETS.nodeName(),
            PML_FUNCTIONS_TARGET.nodeName(),
            PML_CONSTANTS_TARGET.nodeName(),
            OBLIGATIONS_TARGET.nodeName(),
            PROHIBITIONS_TARGET.nodeName()
    ));

    public static final Set<String> AL_NODE_CONSTANT_NAMES = new HashSet<>(List.of(
            ADMIN_POLICY.constantName(),
            ADMIN_POLICY_TARGET.constantName(),
            POLICY_CLASS_TARGETS.constantName(),
            PML_FUNCTIONS_TARGET.constantName(),
            PML_CONSTANTS_TARGET.constantName(),
            OBLIGATIONS_TARGET.constantName(),
            PROHIBITIONS_TARGET.constantName()
    ));

    public static boolean isAdminPolicyNodeConstantName(String name) {
        return AL_NODE_CONSTANT_NAMES.contains(name);
    }

    public static boolean isAdminPolicyNodeName(String name) {
        return ALL_NODE_NAMES.contains(name);
    }

    public static String policyClassTargetName(String policyClass) {
        return policyClass + ":target";
    }


    /**
     * Create {@link AdminPolicyNode#ADMIN_POLICY}
     * Create the {@link AdminPolicyNode#ADMIN_POLICY} policy class.<p>
     * Create the {@link AdminPolicyNode#POLICY_CLASS_TARGETS} in the ADMIN_POLICY policy class.<p>
     * Create the {@link AdminPolicyNode#ADMIN_POLICY_TARGET} in the POLICY_CLASS_TARGETS object attribute.<p>
     * Create the {@link AdminPolicyNode#PML_CONSTANTS_TARGET} in the ADMIN_POLICY policy class.<p>
     * Create the {@link AdminPolicyNode#PML_FUNCTIONS_TARGET} in the ADMIN_POLICY policy class.<p>
     * Create the {@link AdminPolicyNode#PROHIBITIONS_TARGET} in the ADMIN_POLICY policy class.<p>
     * Create the {@link AdminPolicyNode#OBLIGATIONS_TARGET} in the ADMIN_POLICY policy class.<p>
     *
     * Verify that all AdminPolicy constants are defined as PML constants in the policy. <p>
     *
     * Verify that all policy classes have a target attribute in the admin policy.
     *
     * @param verifier The verifier used to verify the admin policy nodes exist.
     * @param graphStore The graph store used to verify nodes.
     * @throws PMException If there is an error verifying any element of the admin policy.
     */
    public static void verify(Verifier verifier, GraphStore graphStore) throws PMException {
        verifyAdminPolicy(verifier);

        verifyPolicyClasses(graphStore);
    }

    private static void verifyPolicyClasses(GraphStore graphStore)
            throws PMBackendException, NodeDoesNotExistException, InvalidAssignmentException, NodeNameExistsException,
                   AssignmentCausesLoopException {
        List<String> policyClasses = graphStore.getPolicyClasses();
        for (String pc : policyClasses) {
            String repOA = policyClassTargetName(pc);
            if (graphStore.nodeExists(repOA)) {
                continue;
            }

            graphStore.createObjectAttribute(repOA, POLICY_CLASS_TARGETS.nodeName());
        }
    }

    private static void verifyAdminPolicy(Verifier verifier) throws AdminPolicyVerificationException {
        try {
            verifier.verifyAdminPolicyClassNode();

            verifier.verifyAdminPolicyAttribute(POLICY_CLASS_TARGETS, ADMIN_POLICY);
            verifier.verifyAdminPolicyAttribute(ADMIN_POLICY_TARGET, POLICY_CLASS_TARGETS);
            verifier.verifyAdminPolicyAttribute(PML_FUNCTIONS_TARGET, ADMIN_POLICY);
            verifier.verifyAdminPolicyAttribute(PML_CONSTANTS_TARGET, ADMIN_POLICY);
            verifier.verifyAdminPolicyAttribute(OBLIGATIONS_TARGET, ADMIN_POLICY);
            verifier.verifyAdminPolicyAttribute(PROHIBITIONS_TARGET, ADMIN_POLICY);

            verifier.verifyAdminPolicyConstant(ADMIN_POLICY);
            verifier.verifyAdminPolicyConstant(POLICY_CLASS_TARGETS);
            verifier.verifyAdminPolicyConstant(ADMIN_POLICY_TARGET);
            verifier.verifyAdminPolicyConstant(PML_FUNCTIONS_TARGET);
            verifier.verifyAdminPolicyConstant(PML_CONSTANTS_TARGET);
            verifier.verifyAdminPolicyConstant(PROHIBITIONS_TARGET);
            verifier.verifyAdminPolicyConstant(OBLIGATIONS_TARGET);
        } catch (PMException e) {
            throw new AdminPolicyVerificationException(e);
        }
    }

    public interface Verifier {
        void verifyAdminPolicyClassNode() throws PMException;

        void verifyAdminPolicyAttribute(AdminPolicyNode node, AdminPolicyNode parent) throws PMException;

        void verifyAdminPolicyConstant(AdminPolicyNode constant) throws PMException;
    }


}
