package gov.nist.csd.pm.core.pap.admin;

import static gov.nist.csd.pm.core.common.graph.node.NodeType.OA;
import static gov.nist.csd.pm.core.common.graph.node.NodeType.PC;
import static gov.nist.csd.pm.core.pap.admin.AdminPolicyNode.PM_ADMIN_BASE_OA;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.store.GraphStore;
import java.util.Collection;

public class AdminPolicy {

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
