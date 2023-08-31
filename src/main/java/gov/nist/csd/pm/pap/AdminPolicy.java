package gov.nist.csd.pm.pap;

import gov.nist.csd.pm.policy.exceptions.*;
import gov.nist.csd.pm.policy.model.graph.relationships.InvalidAssignmentException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AdminPolicy {

    public enum Node {
        ADMIN_POLICY("pm_admin:policy"),
        ADMIN_POLICY_TARGET("pm_admin:policy:target"),
        POLICY_CLASSES_OA("pm_admin:POLICY_CLASSES"),
        PML_FUNCTIONS_TARGET("pm_admin:FUNCTIONS"),
        PML_CONSTANTS_TARGET("pm_admin:CONSTANTS"),
        OBLIGATIONS_TARGET("pm_admin:OBLIGATIONS"),
        PROHIBITIONS_TARGET("pm_admin:PROHIBITIONS");

        private final String value;

        Node(String value) {
            this.value = value;
        }

        public static Node fromNodeName(String child) {
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

    public static final Set<String> ALL_NODE_NAMES = new HashSet<>(List.of(
            Node.ADMIN_POLICY.value,
            Node.ADMIN_POLICY_TARGET.value,
            Node.POLICY_CLASSES_OA.value,
            Node.PML_FUNCTIONS_TARGET.value,
            Node.PML_CONSTANTS_TARGET.value,
            Node.OBLIGATIONS_TARGET.value,
            Node.PROHIBITIONS_TARGET.value
    ));

    public static final Set<String> AL_NODE_CONSTANT_NAMES = new HashSet<>(List.of(
            Node.ADMIN_POLICY.constantName(),
            Node.ADMIN_POLICY_TARGET.constantName(),
            Node.POLICY_CLASSES_OA.constantName(),
            Node.PML_FUNCTIONS_TARGET.constantName(),
            Node.PML_CONSTANTS_TARGET.constantName(),
            Node.OBLIGATIONS_TARGET.constantName(),
            Node.PROHIBITIONS_TARGET.constantName()
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
     * Create {@link AdminPolicy.Node#ADMIN_POLICY}
     * Create the {@link AdminPolicy.Node#ADMIN_POLICY} policy class.<p>
     * Create the {@link AdminPolicy.Node#POLICY_CLASSES_OA} in the ADMIN_POLICY policy class.<p>
     * Create the {@link AdminPolicy.Node#ADMIN_POLICY_TARGET} in the POLICY_CLASSES_OA object attribute.<p>
     * Create the {@link AdminPolicy.Node#PML_CONSTANTS_TARGET} in the ADMIN_POLICY policy class.<p>
     * Create the {@link AdminPolicy.Node#PML_FUNCTIONS_TARGET} in the ADMIN_POLICY policy class.<p>
     * Create the {@link AdminPolicy.Node#PROHIBITIONS_TARGET} in the ADMIN_POLICY policy class.<p>
     * Create the {@link AdminPolicy.Node#OBLIGATIONS_TARGET} in the ADMIN_POLICY policy class.<p>
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

            graphStore.createObjectAttribute(repOA, Node.POLICY_CLASSES_OA.value);
        }
    }

    private static void verifyAdminPolicy(Verifier verifier) throws AdminPolicyVerificationException {
        try {
            verifier.verifyAdminPolicyClassNode();

            verifier.verifyAdminPolicyAttribute(Node.POLICY_CLASSES_OA, Node.ADMIN_POLICY);
            verifier.verifyAdminPolicyAttribute(Node.ADMIN_POLICY_TARGET, Node.POLICY_CLASSES_OA);
            verifier.verifyAdminPolicyAttribute(Node.PML_FUNCTIONS_TARGET, Node.ADMIN_POLICY);
            verifier.verifyAdminPolicyAttribute(Node.PML_CONSTANTS_TARGET, Node.ADMIN_POLICY);
            verifier.verifyAdminPolicyAttribute(Node.OBLIGATIONS_TARGET, Node.ADMIN_POLICY);
            verifier.verifyAdminPolicyAttribute(Node.PROHIBITIONS_TARGET, Node.ADMIN_POLICY);

            verifier.verifyAdminPolicyConstant(Node.ADMIN_POLICY);
            verifier.verifyAdminPolicyConstant(Node.POLICY_CLASSES_OA);
            verifier.verifyAdminPolicyConstant(Node.ADMIN_POLICY_TARGET);
            verifier.verifyAdminPolicyConstant(Node.PML_FUNCTIONS_TARGET);
            verifier.verifyAdminPolicyConstant(Node.PML_CONSTANTS_TARGET);
            verifier.verifyAdminPolicyConstant(Node.PROHIBITIONS_TARGET);
            verifier.verifyAdminPolicyConstant(Node.OBLIGATIONS_TARGET);
        } catch (PMException e) {
            throw new AdminPolicyVerificationException(e);
        }
    }

    public interface Verifier {
        void verifyAdminPolicyClassNode() throws PMException;

        void verifyAdminPolicyAttribute(Node node, Node parent) throws PMException;

        void verifyAdminPolicyConstant(Node constant) throws PMException;
    }


}
