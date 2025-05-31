package gov.nist.csd.pm.core.pap.admin;

import static gov.nist.csd.pm.core.common.graph.node.NodeType.OA;
import static gov.nist.csd.pm.core.common.graph.node.NodeType.PC;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.store.GraphStore;
import java.util.Collection;

public enum AdminPolicyNode {

    PM_ADMIN_PC(-1, "PM_ADMIN"),
    PM_ADMIN_BASE_OA(-2, "PM_ADMIN:base"),
    PM_ADMIN_POLICY_CLASSES(-3, "PM_ADMIN:policy_classes"),
    PM_ADMIN_OBLIGATIONS(-4, "PM_ADMIN:obligations"),
    PM_ADMIN_PROHIBITIONS(-5, "PM_ADMIN:prohibitions"),
    PM_ADMIN_OPERATIONS(-6, "PM_ADMIN:operations"),
    PM_ADMIN_ROUTINES(-7, "PM_ADMIN:routines");

    private final long id;
    private final String name;

    AdminPolicyNode(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public String constantName() {
        return name();
    }

    public String nodeName() {
        return name;
    }

    public long nodeId() {
        return id;
    }

    public static boolean isAdminPolicyNode(long id) {
        for (AdminPolicyNode node : values()) {
            if (node.id == id) {
                return true;
            }
        }

        return false;
    }

    public static void verifyAdminPolicy(GraphStore graphStore) throws PMException {
        long pcId = AdminPolicyNode.PM_ADMIN_PC.nodeId();
        String pcName = AdminPolicyNode.PM_ADMIN_PC.nodeName();

        if (!graphStore.nodeExists(pcId)) {
            graphStore.createNode(AdminPolicyNode.PM_ADMIN_PC.nodeId(), pcName, PC);
        }

        verifyOA(graphStore, PM_ADMIN_BASE_OA, pcId);

        for (AdminPolicyNode adminPolicyNode : AdminPolicyNode.values()) {
            if (adminPolicyNode == AdminPolicyNode.PM_ADMIN_PC || adminPolicyNode == AdminPolicyNode.PM_ADMIN_BASE_OA) {
                continue;
            }

            verifyOA(graphStore, adminPolicyNode, PM_ADMIN_BASE_OA.nodeId());
        }
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
