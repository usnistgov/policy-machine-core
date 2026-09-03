/*
 * This Software (Policy Machine) is being made available as a public service by the
 * National Institute of Standards and Technology (NIST), an Agency of the United
 * States Department of Commerce. This software was developed in part by employees of
 * NIST and in part by NIST contractors. Copyright in portions of this software that
 * were developed by NIST contractors has been licensed or assigned to NIST. Pursuant
 * to Title 17 United States Code Section 105, works of NIST employees are not
 * subject to copyright protection in the United States. However, NIST may hold
 * international copyright in software created by its employees and domestic
 * copyright (or licensing rights) in portions of software that were assigned or
 * licensed to NIST. To the extent that NIST holds copyright in this software, it is
 * being made available under the Creative Commons Attribution 4.0 International
 * license (CC BY 4.0). The disclaimers of the CC BY 4.0 license apply to all parts
 * of the software developed or licensed by NIST.
 *
 * ACCESS THE FULL CC BY 4.0 LICENSE HERE:
 * https://creativecommons.org/licenses/by/4.0/legalcode
 */

package gov.nist.ngac.pm.core.pap.admin;

import static gov.nist.ngac.pm.core.common.graph.node.NodeType.OA;
import static gov.nist.ngac.pm.core.common.graph.node.NodeType.PC;
import static gov.nist.ngac.pm.core.pap.admin.AdminPolicyNode.PM_ADMIN_BASE_OA;
import static gov.nist.ngac.pm.core.pap.admin.AdminPolicyNode.PM_ADMIN_OBLIGATIONS;
import static gov.nist.ngac.pm.core.pap.admin.AdminPolicyNode.PM_ADMIN_OPERATIONS;
import static gov.nist.ngac.pm.core.pap.admin.AdminPolicyNode.PM_ADMIN_PC;
import static gov.nist.ngac.pm.core.pap.admin.AdminPolicyNode.PM_ADMIN_POLICY_CLASSES;
import static gov.nist.ngac.pm.core.pap.admin.AdminPolicyNode.PM_ADMIN_PROHIBITIONS;
import static gov.nist.ngac.pm.core.pap.admin.AdminPolicyNode.PM_ADMIN_ROUTINES;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.store.GraphStore;
import java.util.Collection;

/**
 * Creates and verifies the fixed {@link AdminPolicyNode}s and assignments every policy has.
 */
public class AdminPolicy {

    /**
     * Checks whether the given assignment is one of the fixed admin policy assignments.
     *
     * @param asc the ascendant node id
     * @param desc the descendant node id
     * @return whether the assignment is a fixed admin policy assignment
     */
    public static boolean isAdminPolicyAssignment(long asc, long desc) {
        boolean isBaseToPc = asc == PM_ADMIN_BASE_OA.nodeId() && desc == PM_ADMIN_PC.nodeId();
        boolean isPcsToBase = asc == PM_ADMIN_POLICY_CLASSES.nodeId() && desc == PM_ADMIN_BASE_OA.nodeId();
        boolean isOpsToBase = asc == PM_ADMIN_OPERATIONS.nodeId() && desc == PM_ADMIN_BASE_OA.nodeId();
        boolean isRoutinesToBase = asc == PM_ADMIN_ROUTINES.nodeId() && desc == PM_ADMIN_BASE_OA.nodeId();
        boolean isObligationsToBase = asc == PM_ADMIN_OBLIGATIONS.nodeId() && desc == PM_ADMIN_BASE_OA.nodeId();
        boolean isProhibitionsToBase = asc == PM_ADMIN_PROHIBITIONS.nodeId() && desc == PM_ADMIN_BASE_OA.nodeId();
        return isBaseToPc || isPcsToBase || isOpsToBase || isRoutinesToBase || isObligationsToBase || isProhibitionsToBase;
    }

    /**
     * Ensures every {@link AdminPolicyNode} exists in the graph, creating and assigning any that are
     * missing.
     *
     * @param graphStore the graph store to verify
     * @throws PMException if creating a node or assignment fails
     */
    public static void verifyAdminPolicy(GraphStore graphStore) throws PMException {
        graphStore.beginTx();

        long pcId = AdminPolicyNode.PM_ADMIN_PC.nodeId();
        String pcName = AdminPolicyNode.PM_ADMIN_PC.nodeName();

        if (!graphStore.nodeExists(pcId)) {
            graphStore.createNode(AdminPolicyNode.PM_ADMIN_PC.nodeId(), pcName, PC);
        }

        verifyOA(graphStore, PM_ADMIN_BASE_OA, pcId);

        for (AdminPolicyNode adminPolicyNode : AdminPolicyNode.values()) {
            if (adminPolicyNode == AdminPolicyNode.PM_ADMIN_PC || adminPolicyNode == PM_ADMIN_BASE_OA) {
                continue;
            }

            verifyOA(graphStore, adminPolicyNode, PM_ADMIN_BASE_OA.nodeId());
        }

        graphStore.commit();
    }

    private static void verifyOA(GraphStore graphStore, AdminPolicyNode adminPolicyNode, long parent) throws PMException {
        long oaId = adminPolicyNode.nodeId();
        String oaName = adminPolicyNode.nodeName();
        if (!graphStore.nodeExists(oaId)) {
            graphStore.createNode(oaId, oaName, OA);
        }

        Collection<Long> descendants = graphStore.getAdjacentDescendants(oaId);
        if (!descendants.contains(parent)) {
            graphStore.createAssignment(oaId, parent);
        }
    }

}
