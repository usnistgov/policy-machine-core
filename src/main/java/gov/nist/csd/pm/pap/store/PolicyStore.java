package gov.nist.csd.pm.pap.store;

import gov.nist.csd.pm.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.tx.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.stream.LongStream;

import static gov.nist.csd.pm.common.graph.node.NodeType.OA;
import static gov.nist.csd.pm.common.graph.node.NodeType.PC;

public interface PolicyStore extends Transactional {

    GraphStore graph() throws PMException;
    ProhibitionsStore prohibitions() throws PMException;
    ObligationsStore obligations() throws PMException;
    OperationsStore operations() throws PMException;
    RoutinesStore routines() throws PMException;

    void reset() throws PMException;

    default void verifyAdminPolicy() throws PMException {
        long pcId = AdminPolicyNode.PM_ADMIN_PC.nodeId();
        String pcName = AdminPolicyNode.PM_ADMIN_PC.nodeName();

        if (!graph().nodeExists(pcId)) {
            graph().createNode(AdminPolicyNode.PM_ADMIN_PC.nodeId(), pcName, PC);
        }

        long oaId = AdminPolicyNode.PM_ADMIN_OBJECT.nodeId();
        String oaName = AdminPolicyNode.PM_ADMIN_OBJECT.nodeName();
        if (!graph().nodeExists(oaId)) {
            graph().createNode(oaId, oaName, OA);
        }

        Collection<Long> descendants = graph().getAdjacentDescendants(oaId);
        if (!descendants.contains(pcId)) {
            graph().createAssignment(oaId, pcId);
        }
    }
}
