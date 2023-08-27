package gov.nist.csd.pm.pap;

import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.exceptions.PMException;

import java.util.List;

public class AdminPolicy {

    public static final String ADMIN_POLICY = "admin policy";
    public static final String POLICY_CLASSES_OA = policyClassObjectAttributeName(ADMIN_POLICY);
    public static final String PML_FUNCTIONS_TARGET = "pml functions target";
    public static final String PML_CONSTANTS_TARGET = "pml constants target";
    public static final String ADMIN_POLICY_TARGET = "admin policy target";

    public static String policyClassObjectAttributeName(String policyClass) {
        return policyClass + "_oa";
    }

    public static void enable(Policy policy) throws PMException {
        if (!policy.graph().nodeExists(ADMIN_POLICY)) {
            // admin policy pc and oa will be created
            policy.graph().createPolicyClass(ADMIN_POLICY);
        }

        if (!policy.graph().nodeExists(POLICY_CLASSES_OA)) {
            policy.graph().createObjectAttribute(POLICY_CLASSES_OA, ADMIN_POLICY);
        }

        if (!policy.graph().nodeExists(PML_FUNCTIONS_TARGET)) {
            policy.graph().createObjectAttribute(PML_FUNCTIONS_TARGET, ADMIN_POLICY);
        }

        if (!policy.graph().nodeExists(PML_CONSTANTS_TARGET)) {
            policy.graph().createObjectAttribute(PML_CONSTANTS_TARGET, ADMIN_POLICY);
        }

        if (!policy.graph().nodeExists(ADMIN_POLICY_TARGET)) {
            policy.graph().createObjectAttribute(ADMIN_POLICY_TARGET, ADMIN_POLICY);
        }

        verifyPolicyClasses(policy);
    }

    public static void verifyPolicyClasses(Policy policy) throws PMException {
        List<String> policyClasses = policy.graph().getPolicyClasses();
        for (String pc : policyClasses) {
            String repOA = policyClassObjectAttributeName(pc);
            if (policy.graph().nodeExists(repOA)) {
                continue;
            }

            policy.graph().createObjectAttribute(repOA, POLICY_CLASSES_OA);
        }
    }
}
