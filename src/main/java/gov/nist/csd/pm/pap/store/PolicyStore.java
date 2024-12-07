package gov.nist.csd.pm.pap.store;

import gov.nist.csd.pm.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.tx.Transactional;

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
        String pc = AdminPolicyNode.PM_ADMIN_PC.nodeName();

        if (!graph().nodeExists(pc)) {
            graph().createNode(pc, PC);
        }

        String oa = AdminPolicyNode.PM_ADMIN_OBJECT.nodeName();
        if (!graph().nodeExists(oa)) {
            graph().createNode(oa, OA);
        }

        if (!graph().getAdjacentDescendants(oa).contains(pc)) {
            graph().createAssignment(oa, pc);
        }
    }
}
