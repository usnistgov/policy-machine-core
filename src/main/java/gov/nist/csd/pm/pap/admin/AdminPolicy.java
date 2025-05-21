package gov.nist.csd.pm.pap.admin;

import gov.nist.csd.pm.common.graph.node.Node;
import gov.nist.csd.pm.common.graph.node.NodeType;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static gov.nist.csd.pm.pap.admin.AdminPolicyNode.PM_ADMIN_OBJECT;
import static gov.nist.csd.pm.pap.admin.AdminPolicyNode.PM_ADMIN_PC;

public class AdminPolicy {

    public static final Set<String> ALL_NODE_NAMES = new HashSet<>(List.of(
            PM_ADMIN_PC.nodeName(),
            PM_ADMIN_OBJECT.nodeName()
    ));

    public static final Set<Node> ALL_NODES = new HashSet<>(List.of(
            new Node(PM_ADMIN_PC.nodeId(), PM_ADMIN_PC.nodeName(), NodeType.PC),
            new Node(PM_ADMIN_OBJECT.nodeId(), PM_ADMIN_OBJECT.nodeName(), NodeType.OA)
    ));

    public static final Set<Long> ALL_NODE_IDS = new HashSet<>(List.of(
            PM_ADMIN_PC.nodeId(),
            PM_ADMIN_OBJECT.nodeId()
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

    public static boolean isAdminPolicyId(long id) {
        return ALL_NODE_IDS.contains(id);
    }

}
