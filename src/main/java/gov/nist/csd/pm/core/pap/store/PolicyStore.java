package gov.nist.csd.pm.core.pap.store;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.tx.Transactional;
import gov.nist.csd.pm.core.pap.admin.AdminPolicyNode;

import java.util.Collection;

import static gov.nist.csd.pm.core.common.graph.node.NodeType.OA;
import static gov.nist.csd.pm.core.common.graph.node.NodeType.PC;

public interface PolicyStore extends Transactional {

    GraphStore graph();
    ProhibitionsStore prohibitions();
    ObligationsStore obligations();
    OperationsStore operations();
    RoutinesStore routines();

    void reset() throws PMException;

}
